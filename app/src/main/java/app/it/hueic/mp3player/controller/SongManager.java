package app.it.hueic.mp3player.controller;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import app.it.hueic.mp3player.model.SongModel;

/**
 * Created by kenhoang on 5/1/17.
 */

public class SongManager {
    final String MEDIA_PATH = new String("/sdcard/Download/");
    private ArrayList<SongModel> songslist = new ArrayList<>();
    /**
    * Function to read all mp3 files from sdcard
    * and store the details in ArrayList
    */
    public ArrayList<SongModel> getPlayList() {
        File home = new File(MEDIA_PATH);
        File[] listOfFile = home.listFiles(new FileExtensionFilter());
        if (listOfFile != null && listOfFile.length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                SongModel model = new SongModel();
                model.title = file.getName().substring(0, (file.getName().length() - 4));
                model.path = file.getPath();
                //Adding each song list to SongList
                songslist.add(model);
            }
        } else {
            System.out.println("Ko co gi");
        }
        //return song list array
        return songslist;
    }
    /**
     * Class to filter files which are having .mp3 extension
     * */
    class FileExtensionFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}
