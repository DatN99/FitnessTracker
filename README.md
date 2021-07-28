# FitnessTracker
App that allows you to see your strength progress, over a graph, by manually-inputted workouts

Users can switch between fragments by selecting on the desired tab using the bottom navigation. The opening fragment will always be the "StartWorkoutFragment" class

How to use app:

Starting a Workout

Recycler View Features:
 * Upon adding a new set, the new set will be displayed right below the previously added set
 * The user can add a duplicate set by clicking on the copy icon which will enter a new Sets object with the corresponding card
 * The user can edit a set by clicking on the pencil icon which will display an addSetDialog with that set's information already written in the corresponding fields
 * The user can delete a set by swiping from right to left on that specific set, and the deletion can be undone immediately by click "undo" on the bottom of the screen
 * Note: the RecyclerView uses "SetAdapter" to make any changes to the Set cards
 *
 * Dialog Features:
 * Upon clicking on the + icon, an dialog fragment will pop up allowing the user to enter the set's name, reps, and weight
 * The user can pick any name they want, but the reps and weight must be numbers otherwise the corresponding EditText will throw an error notifying the use to correct their input
 * Pressing "confirm set" will add the set to the SetsList and RecyclerView

Timer Features:
 * Once started, the timer can be paused or stopped. If paused, the time left is saved while the countdowntimer is cancelled. Once resumed, a new countdowntimer is created with the time left.
 * If stopped, the countdowntimer is cancelled and the time left is reset.
 *
 * The timer has an extra progress bar display that allows the user to see the overall count down progress visualized on a circle
 *
 * Once a timer has started, the user can roam around the app or their phone and the countdowntimer will still continue
 * The timer will be continuously updated as a notification so the user can see the progress on their timer without having to stay on this fragment.
 * The user can click on this notification from outside of the fragment to re-enter the fragment


Seeing Past Workouts
* Features:
 * Each card displays a (time) date and volume
 * Clicking on the down arrow will expand the card and show all of the users sets during that workout

Seeing Progress Graph
 * Features:
 * y-axis = volume
 * x-axis = time/date
 *
 * x-axis labels are rotated for neatness

