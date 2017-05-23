I made the application for devices which are supported by Lollipop and I imported a library (json-simple.jar) in order to parse the JSON data.

1. Structure -The application gets JSON objects with GET method. 
/*
This is what I intended to do, but I could not finish it. 
How makes it possible to use the application offline -When the user launches the application, the application will save all reviews as a file, so that the user can see the reviews without internet connection. Also, it shortens the loading time of the data, because the application requests JSON objects only once when it is launched. But when the user changes the page of the reviews or the sorting/filtering ways, it requests the reviews again. 
*/


2. This will be better. 
Managing the objects by using sqlite; then it can manage the review_id automatically. 
Synchronizing the database file (reviews.db) with current reviews frequently. 
User-friendler UX (e.g. It would be much better when the user could select the count of reviews to see in a page.
/ More various sorting/filtering ways - by reviewers' country)


3. Could not finish some tasks.
Sorting/Filtering ; i implemented them and I checked it by watching logging and it worked well. But it could not refresh the list-view appropriately after the sorting/filtering. I did not have time to fix it. 