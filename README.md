# Tic Tac Toe App

## Student Details

- **Student 1:** Shlok Patel  
  **ID:** 2021A7PS2441G

- **Student 2:** Suraz Kumar  
  **ID:** 2021B2A71602G

---

## Project Description

The **Tic Tac Toe App** is a mobile game application that supports two modes:
1. **Single-player mode:** Play against the app's logic.
2. **Two-player mode:** Compete against another user logged in on a separate device.

### Features
- **Login System:** Users can register, log in, and log out.
- **Dashboard:** Displays the user's scores (wins and losses) and available games to join.
- **Single-player Game:** User competes against a simple AI that makes random moves.
- **Two-player Game:** Two users play on separate devices using Firebase for real-time updates.
- **Forfeit Handling:** Players can forfeit games, updating both players' scores.
- **Accessibility:** Enhanced support for screen readers and accessibility options.

### Known Bugs
- None reported at this time.

---

## Task Summary

### 1. **Login and Firebase Integration**
- Implemented a sign-in screen using email and password.
- User data is stored securely in Firebase.
- Added a "Sign Out" button on all post-login screens to allow users to log out.

### 2. **Single-player Game**
- Created a 3x3 grid UI for the game board.
- Implemented game logic where the app makes random moves as the opponent.
- Dialogs are shown for win, loss, or draw outcomes, and scores are updated on the dashboard.

### 3. **Two-player Game**
- Implemented real-time game updates using Firebase for communication between players.
- The player who creates the game makes the first move.
- Players are notified of game outcomes (win, loss, draw) and can continue playing other games or return to the dashboard.

### 4. **Forfeit Handling**
- Added a confirmation dialog when a player tries to leave the game mid-way.
- Forfeiting updates the opponent's win count and redirects both players to their dashboards.

---

## Hosting and Running Instructions

### Hosting
1. Set up Firebase for authentication and database functionality.
2. Enable email-password authentication in Firebase console.
3. Deploy the app on an Android device or emulator.

### Running
1. Clone the repository from GitHub:
   ```bash
   git clone [repository-url]
   ```
2. Open the project in Android Studio.
3. Replace Firebase configuration files (`google-services.json`) with your own.
4. Build and run the app.

---

## Testing and Accessibility

- **Testing:** Used Espresso to create test cases for login, navigation, and gameplay scenarios.
- **Accessibility Enhancements:**
    - Added descriptive labels for buttons and game elements.
    - Tested the app using TalkBack and Accessibility Scanner to ensure compliance.

---

## Approximate Hours Worked

- **Total Time:** ~15 hours

---

## Pair programming score 4 out of 5

---

## Assignment Rating

- **Difficulty:** 9/10

---

## Attributions

- Firebase integration was referenced from the official Firebase documentation.
- Game logic and UI inspiration taken from Android developer resources.

---

## Screenshots

Include screenshots of the login screen, dashboard, single-player game, and two-player game here (if required by the submission guidelines).
