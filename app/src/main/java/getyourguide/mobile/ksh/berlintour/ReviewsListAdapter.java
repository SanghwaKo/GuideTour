package getyourguide.mobile.ksh.berlintour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KSH on 2017-05-21.
 */

public class ReviewsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Review> reviewsList;
    private int[] ratingResIds = {R.id.star_eins, R.id.star_zwei, R.id.star_drei,
            R.id.star_vier, R.id.star_fuenf};

    public ReviewsListAdapter(Context context, ArrayList<Review> reviewsList){
        this.context = context;
        this.reviewsList = reviewsList;
    }

    static class ViewHolder {
        private TextView reviewTitle;
        private ImageView[] reviewRating = new ImageView[5];
        private TextView reviewMessage;
        private TextView reviewInfo;
    }

    @Override
    public int getCount() {
        return reviewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.one_review, null);
            ViewHolder holder = new ViewHolder();
            holder.reviewTitle = (TextView)view.findViewById(R.id.review_title);
            for(int i=0; i<ratingResIds.length; i++){
                holder.reviewRating[i] = (ImageView)view.findViewById(ratingResIds[i]);
            }
            holder.reviewMessage = (TextView)view.findViewById(R.id.review_message);
            holder.reviewInfo = (TextView)view.findViewById(R.id.review_info);
            view.setTag(holder);
        }

        Review review = reviewsList.get(position);
        if(review != null){
            ViewHolder holder = (ViewHolder)view.getTag();
            holder.reviewTitle.setText(review.getTitle());
            holder.reviewInfo.setText(review.getReviewInformation());
            holder.reviewMessage.setText(review.getMessage());
            for(int i=0; i<5; i++){
                holder.reviewRating[i].setImageResource(R.drawable.zero_point);
            }

            if(review.getRating() >= 1f){
                holder.reviewRating[0].setImageResource(R.drawable.one_point);
                if(review.getRating() >= 2f){
                    holder.reviewRating[1].setImageResource(R.drawable.one_point);
                    if(review.getRating() >= 3f){
                        holder.reviewRating[2].setImageResource(R.drawable.one_point);
                        if(review.getRating() >= 4f){
                            holder.reviewRating[3].setImageResource(R.drawable.one_point);
                            if(review.getRating() == 5f){
                                holder.reviewRating[4].setImageResource(R.drawable.one_point);
                            }else if(review.getRating() == 4.5f){
                                holder.reviewRating[4].setImageResource(R.drawable.half_point);
                            }
                        }else{
                            if(review.getRating() == 3.5f){
                                holder.reviewRating[3].setImageResource(R.drawable.half_point);
                            }
                        }
                    }else{
                        if(review.getRating() == 2.5f){
                            holder.reviewRating[2].setImageResource(R.drawable.half_point);
                        }
                    }
                }else{
                    if(review.getRating() == 1.5f){
                        holder.reviewRating[1].setImageResource(R.drawable.half_point);
                    }
                }
            }else{
                if(review.getRating() == 0.5f){
                    holder.reviewRating[0].setImageResource(R.drawable.half_point);
                }
            }
        }
        return view;
    }
}
