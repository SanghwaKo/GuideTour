package getyourguide.mobile.ksh.berlintour;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by KSH on 2017-05-21.
 */

public class ReviewsListActivity extends Activity {
    private String TAG = "";
    private String mResponseFromUrl = "";
    private ArrayList<Review> mReviews;

    // total reviews info
    private boolean mStatus = false;
    private int mTotalReviews;
    private int mCount = 60;
    private int mPage = 0;
    private int mTotalPage = 0;

    // UI Components
    private ReviewsListAdapter mReviewsListAdapter;
    private ListView mReviewsListView;

    private ImageView[] mTotalRatings = new ImageView[5];
    private TextView mTotalRatingText;
    private TextView mTotalReviewsCount;
    private int[] mTotalRatingsIds = {R.id.total_rating_ein, R.id.total_rating_zwei, R.id.total_rating_drei,
            R.id.total_rating_vier, R.id.total_rating_fuenf};
    private TextView mCurrentPage;

    // String[] travelType
    private String[] mTravelerTypeTag = {Constant.TRA_TYPE_ALONE, Constant.TRA_TYPE_YOUNG, Constant.TRA_TYPE_OLD,
                                            Constant.TRA_TYPE_FRIENDS, Constant.TRA_TYPE_COUPLE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list);

        mReviewsListView = (ListView)findViewById(R.id.review_list);
        for(int i=0; i<mTotalRatingsIds.length; i++){
            mTotalRatings[i] = (ImageView)findViewById(mTotalRatingsIds[i]);
            mTotalRatings[i].setImageResource(R.drawable.zero_point);
        }
        mTotalReviewsCount = (TextView)findViewById(R.id.total_reviews_count);
        mTotalRatingText = (TextView)findViewById(R.id.total_rating_txt);
        mCurrentPage = (TextView)findViewById(R.id.current_page);

        TAG = getLocalClassName();
        mReviews = new ArrayList<>();

        refreshUrl(this, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setReviewsListWithFiltering(int selectedFilter) {
        Iterator<Review> iterator = mReviews.iterator();

        if(selectedFilter == 10){
            mPage = 0;
            refreshUrl(ReviewsListActivity.this, true);
            return;
        }

        if(selectedFilter == 0 || selectedFilter == 1){
            // Filtered by written date

        }else if(selectedFilter >= 2 && selectedFilter <5){
            // Filtered by rating
            float minValue = 0f, maxValue = 5f;
            switch (selectedFilter){
                case 2:
                    minValue = 4f;
                    maxValue = 5f;
                    break;
                case 3:
                    minValue = 2.5f;
                    maxValue = 4f;
                    break;
                case 4:
                    minValue = 0f;
                    maxValue = 2.5f;
                    break;
            }
            while(iterator.hasNext()){
                Review review = iterator.next();
                if(!(review.getRating() >= minValue && review.getRating() <= maxValue)){
                    iterator.remove();
                }
            }
        }else{
            // Filtered by traveler type
            String condition = mTravelerTypeTag[selectedFilter-5];
            while(iterator.hasNext()){
                Review review = iterator.next();
                if(!(review.getTravelerType().equals(condition))){
                    iterator.remove();
                }
            }
        }

        if(Debug.DEBUG){
            for(int i=0; i<mReviews.size(); i++){
                Log.d(TAG, "After Filtering : " + mReviews.get(i).toString());
            }
        }
        mTotalReviews = mReviews.size();
        mReviewsListAdapter.notifyDataSetChanged();
        refreshUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_downloading:
                // download data so that the users can see reviews without internet
                return true;
            case R.id.action_writing:
                Intent writingIntent = new Intent(ReviewsListActivity.this, WritingReviewActivity.class);
                startActivity(writingIntent);
                return true;
            case R.id.action_filtering:
                refreshUrl(ReviewsListActivity.this, false);
                final AlertDialog.Builder filteringBuilder = new AlertDialog.Builder(ReviewsListActivity.this);
                filteringBuilder.setTitle(R.string.filtering)
                        .setItems(R.array.filtering_by, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int filteringBy) {
                                // Changed how we sort the reviews
                                setReviewsListWithFiltering(filteringBy);
                                dialogInterface.cancel();
                            }
                        }).create();
                filteringBuilder.show();
                return true;
            case R.id.action_sorting:
                // There is no change in the review-list. - no need to refresh the list
                AlertDialog.Builder sortingBuilder = new AlertDialog.Builder(ReviewsListActivity.this);
                sortingBuilder.setTitle(R.string.sorting)
                        .setItems(R.array.sorting_by, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int sortingBy) {
                                // Changed how we sort the reviews
                                if(sortingBy == 2){
                                    dialogInterface.cancel();
                                    Collections.sort(mReviews);
                                    mReviewsListAdapter.notifyDataSetChanged();
                                }
                            }
                        }).create();
                sortingBuilder.show();
                return true;
            case R.id.action_pre_page:
                if(mPage >= 1){
                    mPage--;
                    refreshUrl(ReviewsListActivity.this, true);
                }
                if(Debug.DEBUG){
                    Log.d(TAG, "currentPage : " + mPage);
                }

                return true;
            case R.id.action_next_page:
                if(mPage < (mTotalPage-1)){
                    mPage++;
                    refreshUrl(ReviewsListActivity.this, true);
                }
                if(Debug.DEBUG){
                    Log.d(TAG, "currentPage : " + mPage);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshUrl(Context context, boolean viewRefresh){
        // viewRefresh == false :: getAllReviews again, but do not refresh the view
        String url = "https://www.getyourguide.com/berlin-l17/tempelhof-2-hour-airport-history-tour-berlin-airlift-more-t23776/reviews.json?";
        url = url + "count=" + mCount;
        url = url + "&page=" + mPage;

        if(Debug.DEBUG){
            Log.d(TAG, "url to connect : " + url);
        }
        final GetData getData = new GetData(context, url);
        getData.execute();

        if(viewRefresh){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(!getData.isRunning){
                        mReviewsListAdapter.notifyDataSetChanged();
                    }
                }
            });
            thread.start();
        }
    }

    private class GetData extends AsyncTask<Void, Void, Void>{
        private String url;
        private ProgressDialog progressDialog;
        public boolean isRunning = true;

        public GetData(Context context, String url){
            this.url = url;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            isRunning = false;
            progressDialog.dismiss();
            progressDialog = null;

            mReviewsListAdapter = new ReviewsListAdapter(ReviewsListActivity.this, mReviews);
            mReviewsListView.setAdapter(mReviewsListAdapter);
            refreshUI();
        }

        @Override
        protected Void doInBackground(Void... params) {
            UrlConnection urlConnection = new UrlConnection(url);
            mResponseFromUrl = urlConnection.getResponse();
            parsingResponse(mResponseFromUrl);

            return null;
        }
    }

    private void refreshUI(){
        float sum = 0.0f;
        float avgRatings = 0.0f;
        for(int i=0; i<mReviews.size(); i++){
            sum += mReviews.get(i).getRating();
        }
        avgRatings = Math.round(sum / mReviews.size() * 100);
        avgRatings /= 100;

        mTotalReviewsCount.setText(getString(R.string.cnt_reviews).replace("%%DD", mTotalReviews + ""));
        mTotalRatingText.setText(getString(R.string.rating).replace("%%DD", avgRatings + ""));
        mTotalPage = (int)(Math.ceil((double)mTotalReviews / mCount));
        mCurrentPage.setText(getString(R.string.page).replace("%%DD", "" + (1+mPage)).replace("%%TT", "" + mTotalPage));

        for(int i=0; i<mTotalRatingsIds.length; i++){
            mTotalRatings[i].setImageResource(R.drawable.zero_point);
        }

        if(avgRatings >= 1f){
            mTotalRatings[0].setImageResource(R.drawable.one_point);
            if(avgRatings >= 2f){
                mTotalRatings[1].setImageResource(R.drawable.one_point);
                if(avgRatings >= 3f){
                    mTotalRatings[2].setImageResource(R.drawable.one_point);
                    if(avgRatings >= 4f){
                        mTotalRatings[3].setImageResource(R.drawable.one_point);
                        if(avgRatings == 5f){
                            mTotalRatings[4].setImageResource(R.drawable.one_point);
                        }else{
                            if(avgRatings >= 4.5){
                                mTotalRatings[4].setImageResource(R.drawable.half_point);
                            }
                        }
                    }else{
                        if(avgRatings >= 3.5f){
                            mTotalRatings[3].setImageResource(R.drawable.half_point);
                        }
                    }
                }else{
                    if(avgRatings >= 2.5f){
                        mTotalRatings[2].setImageResource(R.drawable.half_point);
                    }
                }
            }else{
                if(avgRatings >= 1.5f){
                    mTotalRatings[1].setImageResource(R.drawable.half_point);
                }
            }
        }else{
            if(avgRatings >= 0.5f){
                mTotalRatings[0].setImageResource(R.drawable.half_point);
            }
        }
    }

    private void parsingResponse(String response){
        JSONParser parser = new JSONParser();
        mReviews.clear();
        try{
            JSONObject obj = (JSONObject)parser.parse(response);
            if(obj.containsKey(Constant.TAG_STATUS)){
                String statusStr = obj.get(Constant.TAG_STATUS).toString();
                if(statusStr.equals("true")){
                    mStatus = true;
                }
                if(Debug.DEBUG){
                    Log.d(TAG, "status : " + mStatus);
                }
            }
            if (obj.containsKey(Constant.TAG_TOT_REVIEWS)){
                String totalStr = obj.get(Constant.TAG_TOT_REVIEWS).toString();
                try{
                    mTotalReviews = Integer.parseInt(totalStr);
                    if(Debug.DEBUG){
                        Log.d(TAG, "totalReviews : " + mTotalReviews);
                    }
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
            }

            if (obj.containsKey(Constant.TAG_DATA)) {
                JSONArray dataArr = (JSONArray)obj.get(Constant.TAG_DATA);

                for(int i=0;i<dataArr.size(); i++) {
                    Review newReview = new Review();
                    JSONObject dataObj = (JSONObject) dataArr.get(i);
                    if (dataObj.containsKey(Constant.TAG_ID)) {
                        String idStr = dataObj.get(Constant.TAG_ID).toString();
                        try {
                            newReview.setReviewId(Integer.parseInt(idStr));
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (dataObj.containsKey(Constant.TAG_RATING)) {
                        String ratingStr = dataObj.get(Constant.TAG_RATING).toString();
                        try {
                            newReview.setRating(Float.parseFloat(ratingStr));
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (dataObj.containsKey(Constant.TAG_TITLE)) {
                        String title = dataObj.get(Constant.TAG_TITLE).toString();
                        newReview.setTitle(title);
                    }

                    if (dataObj.containsKey(Constant.TAG_MSG)) {
                        String msg = dataObj.get(Constant.TAG_MSG).toString();
                        newReview.setMessage(msg);
                    }

                    if (dataObj.containsKey(Constant.TAG_AUTH)) {
                        String auth = dataObj.get(Constant.TAG_AUTH).toString();
                        newReview.setAuthor(auth);
                    }

                    if (dataObj.containsKey(Constant.TAG_IS_FOR)) {
                        String isForeignLanguage = dataObj.get(Constant.TAG_IS_FOR).toString();
                        if (isForeignLanguage.equals("true")) {
                            newReview.setIsForeignLanguage();
                        }
                    }

                    if (dataObj.containsKey(Constant.TAG_DATE_FORMATTED)) {
                        String dateFormatted = dataObj.get(Constant.TAG_DATE_FORMATTED).toString();
                        newReview.setWrittenDate(dateFormatted);
                    }

                    if(dataObj.containsKey(Constant.TAG_TRA_TYPE)){
                        if(dataObj.get(Constant.TAG_TRA_TYPE) != null){
                            String travelerType = dataObj.get(Constant.TAG_TRA_TYPE).toString();
                            newReview.setTravelerType(travelerType);
                        }
                    }

                    if(dataObj.containsKey(Constant.TAG_LANG_CODE)){
                        String languageCode = dataObj.get(Constant.TAG_LANG_CODE).toString();
                        newReview.setLanguageCode(languageCode);
                    }

                    if (dataObj.containsKey(Constant.TAG_REVIEWER_NAME)) {
                        String reviewerName = dataObj.get(Constant.TAG_REVIEWER_NAME).toString();
                        newReview.setReviewerName(reviewerName);
                    }

                    if (dataObj.containsKey(Constant.TAG_REVIEWER_COUN)) {
                        String reviewerCountry = dataObj.get(Constant.TAG_REVIEWER_COUN).toString();
                        newReview.setReviewerCountry(reviewerCountry);
                    }
                    mReviews.add(newReview);

                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
