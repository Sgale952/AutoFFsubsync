package github.sgale;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SubSyncer {
    public static void processSubs() throws IOException, InterruptedException {
        File[] videos = getFiles(Paths.VIDEO);
        File[] subtitles = getFiles(Paths.SUBTITLES);

        if(videos.length==subtitles.length) {
            File[] syncedSubtitles = syncSubs(videos, subtitles);
            if(Boolean.parseBoolean(Paths.OVERLAY_ON_VIDEO.getKey())) {
                applySubs(videos, syncedSubtitles);
            }
        }
        else {
            throw new IllegalArgumentException("Different number of files in folders");
        }
    }

    private static File[] syncSubs(File[] videos, File[] subtitles) throws IOException, InterruptedException {
        File[] syncedSubtitles = new File[subtitles.length];

        for (int i = 0; i<subtitles.length; i++) {
            File syncedSub = executeFFsubsync(videos[i], subtitles[i], i);
            syncedSubtitles[i] = syncedSub;
        }

        return syncedSubtitles;
    }

    private static void applySubs(File[] videos, File[] subtitles) throws IOException, InterruptedException {
        for (int i = 0; i<subtitles.length; i++) {
            executeFFmpeg(videos[i], subtitles[i], i);
        }
    }

    private static File executeFFsubsync(File video, File subtitle, int iteration) throws IOException, InterruptedException {
        String output = getOutput(subtitle, iteration);

        ProcessBuilder pb = new ProcessBuilder(
                "ffsubsync", video.getAbsolutePath(), "-i", subtitle.getAbsolutePath(), "-o", output);
        Process command = pb.start();
        int exitCode = command.waitFor();
        if(exitCode != 0) {
            String errorMessage = getErrorMessage(command);
            throw new IOException("FFsubsync failed with exit code: " + exitCode + ". Error message: " + errorMessage);
        }
        if(Boolean.parseBoolean(Paths.DELETE_INIT_FILES.getKey())) {
            subtitle.delete();
        }
        return new File(output);
    }

    //Not working
    private static void executeFFmpeg(File video, File subtitle, int iteration) throws IOException, InterruptedException {
        String output = getOutput(video, iteration);

        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg", "-i", video.getAbsolutePath(), "-vf", "subtitles=" + subtitle.getAbsolutePath(), output);
        Process command = pb.start();
        int exitCode = command.waitFor();
        if(exitCode != 0) {
            String errorMessage = getErrorMessage(command);
            throw new IOException("FFmpeg failed with exit code: " + exitCode + ". Error message: " + errorMessage);
        }
        if(Boolean.parseBoolean(Paths.DELETE_INIT_FILES.getKey())) {
            video.delete();
        }
    }

    private static String getErrorMessage(Process process) throws IOException {
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            StringBuilder errorMessage = new StringBuilder();
            String line;
            while((line = errorReader.readLine()) != null) {
                errorMessage.append(line).append("\n");
            }
            return errorMessage.toString();
        }
    }

    private static File[] getFiles(Paths dir) {
        File folder = new File(dir.getKey());
        return folder.listFiles();
    }

    private static String getOutput(File input, int iteration) {
        return Paths.SUBTITLES.getKey()+iteration+getExtension(input);
    }

    private static String getExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return fileName.substring(dotIndex);
    }
}
