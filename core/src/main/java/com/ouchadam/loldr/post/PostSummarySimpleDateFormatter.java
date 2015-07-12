package com.ouchadam.loldr.post;

import android.content.res.Resources;

import com.ouchadam.loldr.R;
import com.ouchadam.loldr.data.SimpleDate;

import java.util.concurrent.TimeUnit;

public final class PostSummarySimpleDateFormatter {

    private static final int DAYS_IN_YEAR = 365;

    private final String yearsFormat;
    private final String daysFormat;
    private final String hoursFormat;
    private final String minutesFormat;

    public static PostSummarySimpleDateFormatter newInstance(Resources resources) {
        String yearsFormat = resources.getString(R.string.post_summary_age_years);
        String daysFormat = resources.getString(R.string.post_summary_age_days);
        String hoursFormat = resources.getString(R.string.post_summary_age_hours);
        String minutesFormat = resources.getString(R.string.post_summary_age_minutes);

        return new PostSummarySimpleDateFormatter(yearsFormat, daysFormat, hoursFormat, minutesFormat);
    }

    public PostSummarySimpleDateFormatter(String yearsFormat, String daysFormat, String hoursFormat, String minutesFormat) {
        this.yearsFormat = yearsFormat;
        this.daysFormat = daysFormat;
        this.hoursFormat = hoursFormat;
        this.minutesFormat = minutesFormat;
    }

    public String format(SimpleDate date) {
        long differenceMillis = date.differenceFromNowInMillis();
        if (differenceMillis > 0) {
            return "From the future..!";
        }
        return timeAgo(Math.abs(differenceMillis));
    }

    private String timeAgo(long differenceMillis) {
        long days = TimeUnit.MILLISECONDS.toDays(differenceMillis);
        if (days >= DAYS_IN_YEAR) {
            int wholeYears = (int) (days / DAYS_IN_YEAR);
            return String.format(yearsFormat, wholeYears);
        }

        if (days > 0) {
            return String.format(daysFormat, days);
        }

        long hours = TimeUnit.MILLISECONDS.toHours(differenceMillis);
        if (hours > 0) {
            return String.format(hoursFormat, hours);
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillis);
        if (minutes > 0) {
            return String.format(minutesFormat, minutes);
        }

        return "Just now";
    }

}
