package capybara.racesdispute.habit_tracker;

import androidx.cardview.widget.CardView;

import capybara.racesdispute.habit_tracker.trackers_settings.Models;

public interface TrackersClickListener {

    void onClick(Models trackers);
    void onLongClick(Models trackers, CardView cardView);
}
