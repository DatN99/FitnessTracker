# FitnessTracker
App that allows you to see your strength progress, over a graph, by manually-inputted workouts

CHECK MASTER BRANCH FOR ALL COMMITS AND SOURCE CODE

Users can switch between fragments by selecting on the desired tab using the bottom navigation. The opening fragment will always be the "StartWorkoutFragment" class

How to use app:

Starting a Workout

Recycler View Features:
 * Upon adding a new set, the new set will be displayed right below the previously added set
 * The user can add a duplicate set by clicking on the copy icon which will enter a new Sets object with the corresponding card
 * The user can edit a set by clicking on the pencil icon which will display an addSetDialog with that set's information already written in the corresponding fields
 * The user can delete a set by swiping from right to left on that specific set, and the deletion can be undone immediately by click "undo" on the bottom of the screen
 * Note: the RecyclerView uses "SetAdapter" to make any changes to the Set cards
 
 * Dialog Features:
 * Upon clicking on the + icon, an dialog fragment will pop up allowing the user to enter the set's name, reps, and weight
 * The user can pick any name they want, but the reps and weight must be numbers otherwise the corresponding EditText will throw an error notifying the use to correct their input
 * Pressing "confirm set" will add the set to the SetsList and RecyclerView

<img align="left" src="https://user-images.githubusercontent.com/86983871/143132687-1662deaf-8a67-4674-aa51-68b47b4d4d18.png" width="200" height="400" />
<p align="center">
  <img src="https://user-images.githubusercontent.com/86983871/143133658-d5aaf9c5-bf2a-42f6-acf5-3a8b24dbbfbe.png" width="200" height="400" />
</p>

Timer Features:
 * Once started, the timer can be paused or stopped. If paused, the time left is saved while the countdowntimer is cancelled. Once resumed, a new countdowntimer is created with the time left.
 * If stopped, the countdowntimer is cancelled and the time left is reset.
 * The timer has an extra progress bar display that allows the user to see the overall count down progress visualized on a circle
 * Once a timer has started, the user can roam around the app or their phone and the countdowntimer will still continue
 * The timer will be continuously updated as a notification so the user can see the progress on their timer without having to stay on this fragment.
 * The user can click on this notification from outside of the fragment to re-enter the fragment

<img align="left" src="https://user-images.githubusercontent.com/86983871/143134273-b9bbbbf3-049b-4d1f-aeda-0bc58fe152d0.png" width="200" height="400" />
<p align="center">
  <img src="https://user-images.githubusercontent.com/86983871/143134282-e4faac97-2768-4e68-8eb9-2de9939e38be.png" width="200" height="400" />
</p>
<img src="https://user-images.githubusercontent.com/86983871/143134464-05e472e3-3978-4fa2-8b17-df14c629f055.png" width="200" height="400" />


Seeing Past Workouts
* Features:
 * Each card displays a (time) date and volume
 * Clicking on the down arrow will expand the card and show all of the users sets during that workout

<img align="left" src="https://user-images.githubusercontent.com/86983871/143134641-7f575e74-2224-435b-83d4-0310ae1ec0be.png" width="200" height="400" />
<p align="center">
  <img src="https://user-images.githubusercontent.com/86983871/143134647-331bf2d9-5e42-48dd-a71e-13574c8ec7d0.png" width="200" height="400" />
</p>

Seeing Progress Graph
 * Features:
 * y-axis = volume
 * x-axis = time/date
 * x-axis labels are rotated for neatness

<img align="left" src="https://user-images.githubusercontent.com/86983871/143134822-a5af8607-9d61-4b3f-ab76-754e64d7d676.png" width="200" height="400" />
<p align="center">
  <img src="https://user-images.githubusercontent.com/86983871/143134823-e003f709-fd96-4410-88e4-40120dd42af4.png" width="200" height="400" />
</p>

