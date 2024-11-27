package androidsamples.java.tictactoe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import androidsamples.java.tictactoe.models.GameModel;

public class GameFragment extends Fragment {
  private static final String TAG = "GameFragment";
  private static final int GRID_SIZE = 9;

  private final Button[] mButtons = new Button[GRID_SIZE];

  private NavController mNavController;

  private boolean isSinglePlayer = true;
  private String myChar = "X";
  private String otherChar = "O";
  private boolean myTurn = true;
  private GameModel game;
  private boolean isHost = true;
  private boolean gameEnded = false;
  private DatabaseReference gameReference, userReference;

  private Button quitGame;
  private TextView display;

  private String[] gameArray = new String[]{"", "", "", "", "", "", "", "", ""};

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true); // Needed to display the action menu for this fragment

    // Extract the argument passed with the action in a type-safe way
    GameFragmentArgs args = GameFragmentArgs.fromBundle(getArguments());
    Log.d(TAG, "New game type = " + args.getGameType());

    isSinglePlayer = (args.getGameType().toString().equals("One-Player"));

    userReference = FirebaseDatabase.getInstance("https://tictactoe-e88dd-default-rtdb.firebaseio.com/").getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

    if(!isSinglePlayer){
      gameReference = FirebaseDatabase.getInstance("https://tictactoe-e88dd-default-rtdb.firebaseio.com/").getReference("games").child(args.getGameId());

      gameReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          game = snapshot.getValue(GameModel.class);
          assert game!=null;
          gameArray = (game.getGameArray().toArray(new String[9]));

          if(game.getTurn()==1){
            if(game.getHost().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
              isHost = true;
              myTurn = true;
              myChar = "X";
              otherChar = "O";
            }
            else{
              isHost = false;
              myTurn = false;
              myChar = "O";
              otherChar = "X";
            }
          }
          else{
            if(game.getHost().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
              isHost = true;
              myTurn = false;
              myChar = "X";
              otherChar = "O";
            }
            else{
              isHost = false;
              myTurn = true;
              myChar = "O";
              otherChar = "X";
            }
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      });
    }
    else{
      game = new GameModel("One-Player","Single-player ID");
      Log.i(TAG,"Single playah");
    }
    // Handle the back press by adding a confirmation dialog
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        Log.d(TAG, "Back pressed");

        if(!gameEnded) {
          // TODO show dialog only when the game is still in progress
          AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                  .setTitle(R.string.confirm)
                  .setMessage(R.string.forfeit_game_dialog_message)
                  .setPositiveButton(R.string.yes, (d, which) -> {
                    // TODO update loss count
                    if(!isSinglePlayer){
                      userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                          int val = Integer.parseInt(Objects.requireNonNull(snapshot.child("lost").getValue()).toString());
                          val++;
                          snapshot.getRef().child("lost").setValue(val);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                      });
                    }
                    mNavController.popBackStack();
                  })
                  .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                  .create();
          dialog.show();
        }
        else{
          assert getParentFragment() != null;
          NavHostFragment.findNavController(getParentFragment()).navigateUp();
        }
      }
    };
    requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_game, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    display = view.findViewById(R.id.display_tv);

    if (!isSinglePlayer) {
      boolean check = false;
      for (String s : gameArray) {
        if (!s.isEmpty()) {
          check = true;
          break;
        }
      }
      if (!check) {
        waitForOtherPlayer();
      }
    } else {
      display.setText("Your Turn");
    }

    mNavController = Navigation.findNavController(view);

    mButtons[0] = view.findViewById(R.id.button0);
    mButtons[1] = view.findViewById(R.id.button1);
    mButtons[2] = view.findViewById(R.id.button2);

    mButtons[3] = view.findViewById(R.id.button3);
    mButtons[4] = view.findViewById(R.id.button4);
    mButtons[5] = view.findViewById(R.id.button5);

    mButtons[6] = view.findViewById(R.id.button6);
    mButtons[7] = view.findViewById(R.id.button7);
    mButtons[8] = view.findViewById(R.id.button8);

    for (int i = 0; i < mButtons.length; i++) {
      int finalI = i;
      mButtons[i].setOnClickListener(v -> {
        Log.d(TAG, "Button " + finalI + " clicked");
        // TODO implement listeners
        if(myTurn){
          ((Button) v).setText(myChar);
          gameArray[finalI] = myChar;
          v.setClickable(false);
          display.setText("Waiting...");

          if(!isSinglePlayer){
            updateDB();
            myTurn = updateTurn(game.getTurn());
          }

          int win = checkWin();
          if(win ==1 || win == -1){
            endGame(win);
            return ;
          }
          else if(checkDraw()){
            endGame(0);
            return ;
          }
          myTurn = !myTurn;

          if(isSinglePlayer){
            appPlayTurn();
          }
          else{
            waitForOtherPlayer();
          }
        }
        else{
          Toast.makeText(getContext(), "Please wait for your turn!", Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  private boolean checkDraw() {
    if (checkWin() != 0) return false;
    Log.i("CHECKING WIN IN DRAW", "Complete: " + checkWin());
    for (int i = 0; i < 9; i++) {
      if (gameArray[i].isEmpty()) {
        return false;
      }
    }
    return true;
  }

  private void endGame(int win) {
    switch (win){
      case 1:
        display.setText("You Win :)");
        if(!gameEnded){
          userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              int val = Integer.parseInt(Objects.requireNonNull(snapshot.child("won").getValue()).toString());
              val++;
              snapshot.getRef().child("won").setValue(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
          });

          AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                  .setTitle("Result")
                  .setMessage("Congratulations! You Won :)")
                  .setPositiveButton("OK", (d, which) -> {
                    mNavController.popBackStack();
                  })

                  .create();
          dialog.show();
        }

        break;
      case -1:
        display.setText("You Lost :(");
        if(!gameEnded){
          userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              int val = Integer.parseInt(Objects.requireNonNull(snapshot.child("lost").getValue()).toString());
              val++;
              snapshot.getRef().child("lost").setValue(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
          });
          AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                  .setTitle("Result")
                  .setMessage("Sorry! You Lost :(")
                  .setPositiveButton("OK", (d, which) -> {
                    mNavController.popBackStack();
                  })

                  .create();
          dialog.show();
        }
        break;
      case 0:
        display.setText("Draw -_-");
        if(!gameEnded){
          userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              int val = Integer.parseInt(Objects.requireNonNull(snapshot.child("draw").getValue()).toString());
              val++;
              snapshot.getRef().child("draw").setValue(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
          });
          AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                  .setTitle("Result")
                  .setMessage("You Drew! -_-")
                  .setPositiveButton("OK", (d, which) -> {
                    mNavController.popBackStack();
                  })

                  .create();
          dialog.show();
        }
        break;
      default:
        display.setText("Error");
        break;
    }

    for (int i = 0; i < 9; i++) {
      mButtons[i].setClickable(false);
    }
    gameEnded = true;

    if(!isSinglePlayer)
      updateDB();
  }

  private void appPlayTurn() {
    Random rand = new Random();
    int x = rand.nextInt(9);
    if (checkDraw()) {
      endGame(0);
      return;
    }
    while (!gameArray[x].isEmpty()) x = rand.nextInt(9);
    Log.i("CHECKING CONDITIONS", "Complete");
    gameArray[x] = otherChar;
    mButtons[x].setText(otherChar);
    mButtons[x].setClickable(false);
    myTurn = !myTurn;
    display.setText("Your Turn");
    int win = checkWin();
    if (win == 1 || win == -1) endGame(win);
    else if (checkDraw()) endGame(0);
  }

  private int checkWin() {
    String winChar = "";
    if  (gameArray[0].equals(gameArray[1]) && gameArray[1].equals(gameArray[2]) && !gameArray[0].isEmpty()) winChar = gameArray[0];
    else if (gameArray[3].equals(gameArray[4]) && gameArray[4].equals(gameArray[5]) && !gameArray[3].isEmpty()) winChar = gameArray[3];
    else if (gameArray[6].equals(gameArray[7]) && gameArray[7].equals(gameArray[8]) && !gameArray[6].isEmpty()) winChar = gameArray[6];
    else if (gameArray[0].equals(gameArray[3]) && gameArray[3].equals(gameArray[6]) && !gameArray[0].isEmpty()) winChar = gameArray[0];
    else if (gameArray[4].equals(gameArray[1]) && gameArray[1].equals(gameArray[7]) && !gameArray[1].isEmpty()) winChar = gameArray[1];
    else if (gameArray[2].equals(gameArray[5]) && gameArray[5].equals(gameArray[8]) && !gameArray[2].isEmpty()) winChar = gameArray[2];
    else if (gameArray[0].equals(gameArray[4]) && gameArray[4].equals(gameArray[8]) && !gameArray[0].isEmpty()) winChar = gameArray[0];
    else if (gameArray[6].equals(gameArray[4]) && gameArray[4].equals(gameArray[2]) && !gameArray[2].isEmpty()) winChar = gameArray[2];
    else return 0;

    return (winChar.equals(myChar)) ? 1 : -1;
  }

  private boolean updateTurn(int turn) {
    return (turn == 1) == isHost;
  }

  private void updateDB() {
    gameReference.child("gameArray").setValue(Arrays.asList(gameArray));
    gameReference.child("isOpen").setValue(!gameEnded);
    if (game.getTurn() == 1) {
      game.setTurn(2);
    } else {
      game.setTurn(1);
    }
    gameReference.child("turn").setValue(game.getTurn());
  }

  private void waitForOtherPlayer() {
    display.setText("Waiting...");
    gameReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        GameModel l = snapshot.getValue(GameModel.class);
        game.updateGameArray(l);
        gameArray = (game.getGameArray().toArray(new String[9]));
        updateUI();

        myTurn = updateTurn(game.getTurn());
        if(myTurn) display.setText("Your Turn");
        else display.setText("Waiting...");

        int win = checkWin();
        if(win == 1 || win == -1){
          endGame(win);
        }
        else if(checkDraw()){
          endGame(0);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  private void updateUI() {
    for (int i = 0; i < 9; i++) {
      String v = gameArray[i];
      if (!v.isEmpty()) {
        mButtons[i].setText(v);
        mButtons[i].setClickable(false);
      }
    }
  }

  public static int testCheckWin(String[] gameArr) {
    String myChar = "X";
    String winChar = "";
    if  (gameArr[0].equals(gameArr[1]) && gameArr[1].equals(gameArr[2]) && !gameArr[0].isEmpty()) winChar = gameArr[0];
    else if (gameArr[3].equals(gameArr[4]) && gameArr[4].equals(gameArr[5]) && !gameArr[3].isEmpty()) winChar = gameArr[3];
    else if (gameArr[6].equals(gameArr[7]) && gameArr[7].equals(gameArr[8]) && !gameArr[6].isEmpty()) winChar = gameArr[6];
    else if (gameArr[0].equals(gameArr[3]) && gameArr[3].equals(gameArr[6]) && !gameArr[0].isEmpty()) winChar = gameArr[0];
    else if (gameArr[4].equals(gameArr[1]) && gameArr[1].equals(gameArr[7]) && !gameArr[1].isEmpty()) winChar = gameArr[1];
    else if (gameArr[2].equals(gameArr[5]) && gameArr[5].equals(gameArr[8]) && !gameArr[2].isEmpty()) winChar = gameArr[2];
    else if (gameArr[0].equals(gameArr[4]) && gameArr[4].equals(gameArr[8]) && !gameArr[0].isEmpty()) winChar = gameArr[0];
    else if (gameArr[6].equals(gameArr[4]) && gameArr[4].equals(gameArr[2]) && !gameArr[2].isEmpty()) winChar = gameArr[2];
    else return 0;

    return (winChar.equals(myChar)) ? 1 : -1;
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_logout, menu);
    // this action menu is handled in MainActivity
  }
}