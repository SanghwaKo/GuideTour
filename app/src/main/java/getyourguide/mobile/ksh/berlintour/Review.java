package getyourguide.mobile.ksh.berlintour;

/**
 * Created by KSH on 2017-05-21.
 */

public class Review implements Comparable {
    //Sorting by rating & date

    private int reviewId;
    private float rating;
    private String title;
    private String message;
    private String author;
    private boolean isForeignLanguage;
    private String writtenDate;
    private String travelerType;
    private String languageCode;
    private String reviewerName;
    private String reviewerCountry;

    public Review(){
        reviewId = 0;
        rating = 0.0f;
        title = "";
        message = "";
        author = "";
        isForeignLanguage = false;
        writtenDate = "";
        travelerType = "null";
        languageCode = "us";
        reviewerName = "";
        reviewerCountry = "";
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public boolean getIsForeignLanguage() {
        return isForeignLanguage;
    }

    public void setIsForeignLanguage() {
        // Base language - english // United states, united kingdom, australia
        if(!languageCode.equalsIgnoreCase("US") &&
                !languageCode.equalsIgnoreCase("GB") &&
                ! languageCode.equalsIgnoreCase("AU")){
            isForeignLanguage = true;
        }
    }

    public void setWrittenDate(String writtenDate) {
        this.writtenDate = writtenDate;
    }

    public String getWrittenDate(){
        return writtenDate;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        this.languageCode = languageCode;
        setIsForeignLanguage();
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerName(){
        return reviewerName;
    }

    public void setReviewerCountry(String reviewerCountry) {
        this.reviewerCountry = reviewerCountry;
    }

    public String getReviewerCountry(){
        return getReviewerCountry();
    }

    public String getReviewInformation(){
        return reviewerName + " - " + reviewerCountry + " " + writtenDate;
    }

    public void setTravelerType(String travelerType){
        this.travelerType = travelerType;
    }

    public String getTravelerType(){
        return travelerType;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Review){
            if(hashCode() == obj.hashCode()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return reviewId + getReviewInformation() + " - " + travelerType + " - " + rating;
    }

    @Override
    public int compareTo(Object compared) {
        if(compared instanceof Review){
            Review comparedReview = (Review)compared;
            if(rating - comparedReview.getRating() > 0){
                return 1;
            }else{
                return -1;
            }
            //Sorting by date
           /* SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, y");

            String comparedDateStr = ((Review) compared).getWrittenDate();
            Date comparedDate = new Date();
            Date date = new Date();

            try{
                comparedDate = dateFormat.parse(comparedDateStr);
                date = dateFormat.parse(writtenDate);

                return date.compareTo(comparedDate);
            }catch (ParseException ex){
                ex.printStackTrace();
            }*/

            //Sorting by rating
        }
        return 0;
    }
}
