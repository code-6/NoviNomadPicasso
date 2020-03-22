package stanislav.tun.novinomad.picasso.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.h2.tools.RunScript;
import org.h2.tools.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import stanislav.tun.novinomad.picasso.controllers.TourController;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class BackupUtil {
    private  Logger logger = LoggerFactory.getLogger(BackupUtil.class);

    private  Script script = new Script();

    private  String separator = FileSystems.getDefault().getSeparator();
    private  String folderPath = '.' + separator + "picasso_db" + separator + "backup" + separator;

    @Scheduled(cron = "0 0 21 * * ?")
    public  boolean backup() {
        logger.debug("start create backup of db");
        var success = false;
        var currentDate = LocalDateTime.now();
        var backupName = "dbBackup_" + currentDate.toString() + ".zip";
        try {
            script.runTool("-url", "jdbc:h2:file:~/IdeaProjects/NoviNomadPicasso/picasso_db/picassoDB",
                    "-user", "sa", "-password", "password", "-script", folderPath+backupName,
                    "-options", "compression", "zip");
            logger.debug("backup path: "+folderPath+backupName);
            success = backupCreated(backupName);
            if(success){
                logger.info("backup created");
                deleteOldBackup();
            }else{
                logger.warn("backup not created");
            }

        } catch (SQLException e) {
            logger.error("create backup failed " + ExceptionUtils.getStackTrace(e));
        }
        return success;
    }

    public  boolean deleteOldBackup() {
        logger.debug("start delete old backup");
        var success = false;
        var currentDate = new Date().toInstant().truncatedTo(ChronoUnit.DAYS);
        var folder = new File(folderPath);

        for (File file : folder.listFiles()) {
            logger.debug("check file to delete "+file.getName());
            try {
                var attributes = Files.readAttributes(Paths.get(file.getPath()), BasicFileAttributes.class);
                var fileCreationDate = attributes.creationTime().toInstant().truncatedTo(ChronoUnit.DAYS);

                if(fileCreationDate.isBefore(currentDate)){
                    success = file.delete();
                    if(success)
                        logger.info("old backup deleted "+file.getAbsolutePath());
                    else
                        logger.warn("old backup not deleted.");
                }
            } catch (IOException e) {
                success = false;
                logger.error("unable to get attributes for file: " + file.getAbsolutePath() + " " + ExceptionUtils.getStackTrace(e));
            }
        }

        return success;
    }

    public  boolean backupCreated(String fileName){
        var file = new File(folderPath+fileName);
        logger.debug("check file absPath: "+file.getAbsolutePath());
        logger.debug("check file path: "+file.getPath());
        return file.exists();
    }
}
