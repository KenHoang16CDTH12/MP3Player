package app.it.hueic.mp3player.util;

/**
 * Created by kenhoang on 5/1/17.
 */

public class Util {
    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minute:Seconds
     * */
    public static String milisecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        //Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) ((milliseconds % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        //Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }
        // Prepending 0 to second if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        finalTimerString = finalTimerString + minutes + ":" + secondsString;
        //return timer String
        return finalTimerString;
    }
    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     */
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;
        long currentSeconds  = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
        //calculating percentage
        percentage = (((double)currentSeconds)/totalSeconds) * 100;
        //return percentage
        return percentage.intValue();
    }
    /**
     * Function to change progress to timer
     * @param progress
     * @param totalDuration
     * return curent duration in milliseconds
     */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100 ) * totalDuration);
        //return current duration in milliseconds
        return currentDuration * 1000;
    }
}
