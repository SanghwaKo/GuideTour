I made the application for devices which are supported by Lollippp as you suggested and I imported a library to parse the json. 

1. Structure
-The application got json objects with GET method. 

This is what I intented to do but I could not finish it.
/*
How make it possible to use the application offline
-When the user launches the application, the application will save all reviews by saving them as a file, so that the user can see
 the reviews without internet connection. Also it shortens the time to load all reviews from the url, because the application 
requests json-objects only once when it is launched. But when the user changes the page of the reviews, it requests the reviews 
again.
*/  

2.To make it better.
I wanna manage the objects by using sqlite, then it can manage the review_id automatically. Of course I wanted to synchronize 
the database file with offered reviews. But I did not have enough time in the end. And I could not finish to sort and to filter 
reviews. I implemented them and I checked it by watching logs and it worked well. But I could not refresh the list-view after the
sorting/filtering because of some problems and I did not have time to fix it. Also I wanna add more sorting ways such as sorting 
by reviewers country (filtered by the reviewers country too) . Moreover, it would be much better when the user could select the 
count of reviews to see in a page. 
 
 
 
 
 
 
