package getyourguide.mobile.ksh.berlintour;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;


import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by KSH on 2017-05-21.
 */

public class WritingReviewActivity extends Activity {
    private String TAG = "";
    private CharSequence[] mCountries;
    private String[] mLanguages;
    private String[] mRatingsArr = {"0", "0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5"};
    private String[] mTravelerTypes;
    private String[] mTravelerTypesTag= {Constant.TRA_TYPE_ALONE, Constant.TRA_TYPE_YOUNG, Constant.TRA_TYPE_OLD,
                                            Constant.TRA_TYPE_FRIENDS, Constant.TRA_TYPE_COUPLE};

    // selected by the user
    private int mSelectedTravelerTypeIndex = -1;
    private int mSelectedCountryIndex = -1;
    private int mSelectedRatingIndex = -1;

    // UI Components
    private EditText mReviewTitle;
    private EditText mUserName;
    private EditText mReviewMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale.setDefault(Locale.US);
        TAG = getLocalClassName();
        setContentView(R.layout.write_review);

        String[] countries = Locale.getISOCountries();

        mCountries = new CharSequence[countries.length];
        mLanguages = new String[countries.length];

        for(int i=0; i<countries.length; i++){
            Locale country = new Locale("", countries[i]);
            mCountries[i] = country.getDisplayCountry();
            mLanguages[i] = country.toLanguageTag().substring(4, country.toLanguageTag().length());

            /*if(Debug.DEBUG){
                Log.d(TAG, "Country : " + mCountries[i]+ " - Language : " + mLanguages[i]);
            }*/
        }

        mTravelerTypes = getResources().getStringArray(R.array.traveler_type);

        mReviewTitle = (EditText)findViewById(R.id.new_review_title);
        mUserName = (EditText)findViewById(R.id.new_review_name);
        mReviewMsg = (EditText)findViewById(R.id.new_review_msg);

    }

    public void selectTravelerType(View view){
        if(view.getId() != R.id.new_review_traveler_type){
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(WritingReviewActivity.this);
        builder.setTitle(R.string.write_traveler_type)
                .setItems(mTravelerTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int travelerTypePosition) {
                        mSelectedTravelerTypeIndex = travelerTypePosition;
                    }
                }).create();
        builder.show();
    }

    public void selectCountry(View view){
        if(view.getId() != R.id.new_review_country){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(WritingReviewActivity.this);
        builder.setTitle(R.string.write_user_country)
                .setItems(mCountries, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int countryPosition) {
                        mSelectedCountryIndex = countryPosition;
                    }
                }).create();
        builder.show();
    }

    public void selectRating(View view){
        if(view.getId() != R.id.new_review_rating){
            return;
        }

        final NumberPicker numberPicker = new NumberPicker(WritingReviewActivity.this);
        numberPicker.setMaxValue(mRatingsArr.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(mRatingsArr);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                mSelectedRatingIndex = newVal;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(WritingReviewActivity.this);
        builder.setTitle(R.string.write_rating)
                .setView(numberPicker)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        builder.show();

    }

    public void onSubmit(View view){
        if(view.getId() != R.id.btn_submit){
            return;
        }

        if(mReviewTitle.getText().toString().trim().length() == 0 || mUserName.getText().toString().trim().length() == 0 ||
                mSelectedCountryIndex == -1 || mSelectedRatingIndex == -1){
            Toast.makeText(getApplicationContext(), R.string.warning_fill_form, Toast.LENGTH_SHORT).show();
            return;
        }

        Review newReview = new Review();

        newReview.setTitle(mReviewTitle.getText().toString());
        newReview.setReviewerName(mUserName.getText().toString());
        newReview.setMessage(mReviewMsg.getText().toString());
        newReview.setReviewerCountry(String.valueOf(mCountries[mSelectedCountryIndex]));
        newReview.setLanguageCode(mLanguages[mSelectedCountryIndex]);
        if(mSelectedTravelerTypeIndex >= 0){
            newReview.setTravelerType(mTravelerTypesTag[mSelectedTravelerTypeIndex]);
        }// Default : undefined - it is not a required field.
        newReview.setRating(Float.parseFloat(mRatingsArr[mSelectedRatingIndex]));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, y");
        newReview.setWrittenDate(dateFormat.format(calendar.getTime()));
        newReview.setAuthor(newReview.getReviewerName() + "-" + newReview.getReviewerCountry());

        if(Debug.DEBUG){
         /*   Log.d(TAG, "rating : " + newReview.getRating());
            Log.d(TAG, "userInfo : " + newReview.getReviewInformation());
            Log.d(TAG, "languageCode : " + newReview.getLanguageCode());
            Log.d(TAG, "isForeignLanguage : " + newReview.getIsForeignLanguage());*/

            Log.d(TAG, "review : " + newReview.toString());
        }
        AddNewReview addNewReview = new AddNewReview("https://www.getyourguide.com/berlin-l17/tempelhof-2-hour-airport-history-tour-berlin-airlift-more-t23776/reviews.json",
                makeReviewToJson(newReview));
        addNewReview.sendJSON();
    }

    private JSONObject makeReviewToJson(Review review){
        JSONObject reviewJSON = new JSONObject();
        reviewJSON.put(Constant.TAG_RATING, "" + review.getRating());
        reviewJSON.put(Constant.TAG_TITLE, review.getTitle());
        reviewJSON.put(Constant.TAG_MSG, review.getMessage());
        reviewJSON.put(Constant.TAG_AUTH, review.getAuthor());
        reviewJSON.put(Constant.TAG_IS_FOR, review.getIsForeignLanguage());
        reviewJSON.put(Constant.TAG_DATE_FORMATTED, review.getWrittenDate());
        reviewJSON.put(Constant.TAG_TRA_TYPE, review.getTravelerType());
        reviewJSON.put(Constant.TAG_LANG_CODE, review.getLanguageCode());
        reviewJSON.put(Constant.TAG_REVIEWER_NAME, review.getReviewerName());
        reviewJSON.put(Constant.TAG_REVIEWER_COUN, review.getReviewerCountry());

        return reviewJSON;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
