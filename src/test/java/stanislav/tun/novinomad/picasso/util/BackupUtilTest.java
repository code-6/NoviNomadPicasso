package stanislav.tun.novinomad.picasso.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class BackupUtilTest {

    @Test
    public void backup() {
        var success = BackupUtil.backup();
        assertTrue(success);
    }

    @Test
    public void deleteTest(){
        var success = BackupUtil.deleteOldBackup();
        assertTrue(success);
    }
}