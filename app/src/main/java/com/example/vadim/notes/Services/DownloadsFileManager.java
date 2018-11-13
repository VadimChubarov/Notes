package com.example.vadim.notes.Services;

import android.os.Environment;
import java.io.File;
import java.util.ArrayList;

public class DownloadsFileManager
{
    private String downloadsPath = Environment.getExternalStorageDirectory()+"/Download";
    private String fileType;

    public DownloadsFileManager(){}

    public DownloadsFileManager(String fileType)
    {
        this.fileType = fileType;
    }


    public String [] entryDownloads()
    {
        File file = new File(downloadsPath);
        File[] files = file.listFiles();
        String [] fileNames;

        if (fileType != null)
        {
            ArrayList<String> fileNamesList = new ArrayList<String>();

            for (int i = 0; i < files.length; i++)
            {
                if (files[i].getName().endsWith(fileType))
                {fileNamesList.add(files[i].getName());}
            }

            fileNames = new String[fileNamesList.size()];
            for (int i = 0; i < fileNamesList.size(); i++)
            {fileNames[i] = fileNamesList.get(i);}
        }
        else
            { fileNames = new String[files.length];
              for (int i = 0; i < fileNames.length; i++)
              {fileNames[i] = files[i].getName();}
            }
        return fileNames;
    }

   public String getSelectedFilePath(String fileName)
   {
       String selectedFilePath = null;

       File file = new File(downloadsPath);
       File [] files = file.listFiles();

       for(File current_file : files)
       {
           if(current_file.getName().equals(fileName))
           {
               selectedFilePath = current_file.getPath();
           }
       }
       return selectedFilePath;
   }

}
