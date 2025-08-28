package models.API;


import com.arthenica.ffmpegkit.FFmpegKit;

import java.io.File;

public class AudioConverter {

    private static final int SAMPLE_RATE_INPUT = 16000;

    private static final int CHANNEL_CONFIG_INPUT = 1;

    private static final String CODEC_INPUT = "s16le";

    private static final String CODEC_OUTPUT = "libmp3lame";

    private static final String SAMPLE_RATE_OUTPUT = "32k";

    private static final String OVERWRITE = "-y";

    public static File convertAudio(File inputFile, File outputFile) {

        String pathInputFile = inputFile.getAbsolutePath();
        String pathOutputFile = outputFile.getAbsolutePath();

        String command = OVERWRITE + " -f " + CODEC_INPUT +
                " -ar " + SAMPLE_RATE_INPUT +
                " -ac " + CHANNEL_CONFIG_INPUT +
                " -i " + pathInputFile +
                " -acodec " + CODEC_OUTPUT +
                " -ab " + SAMPLE_RATE_OUTPUT + " " + pathOutputFile;

        FFmpegKit.execute(command);
        FFmpegKit.cancel();

        return outputFile;
    }

}
