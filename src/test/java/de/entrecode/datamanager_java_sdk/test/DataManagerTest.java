package de.entrecode.datamanager_java_sdk.test;

import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.exceptions.ECMalformedDataManagerIDException;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Simon Scherzinger, entrecode GmbH, Stuttgart (Germany), entrecode GmbH, Stuttgart (Germany) on 25.05.15.
 */
public class DataManagerTest {
    /*
    @Test
    public void runExample() throws ECMalformedDataManagerIDException {
        RunningExample rex = new RunningExample();
        rex.main(new String[]{"123"});
    }
     */

    @Test(expected = ECMalformedDataManagerIDException.class)
    public void newDataManagerWithMalformedID() throws ECMalformedDataManagerIDException {
        DataManager dm = new DataManager("id", UUID.randomUUID());
    }

    @Test
    public void newDataManagerWithID() throws ECMalformedDataManagerIDException {
        DataManager dm = new DataManager("beef1234", UUID.randomUUID());
        assertNotNull(dm);
        assertTrue(dm.getClass() == DataManager.class);
        assertNotNull(dm.getToken());
        assertTrue(!dm.getReadOnly());
        assertNotNull(dm.getUrl());
    }

    @Test
    public void newDataManagerWithURL() throws MalformedURLException {
        DataManager dm = new DataManager(new URL("https://datamanager.entrecode.de/api/beefbeef"), UUID.randomUUID());
        assertNotNull(dm);
        assertTrue(dm.getClass() == DataManager.class);
        assertNotNull(dm.getToken());
        assertTrue(!dm.getReadOnly());
        assertNotNull(dm.getID());
    }

    @Test
    public void newDataManagerWithURLAndAppendedSlash() throws MalformedURLException {
        DataManager dm = new DataManager(new URL("https://datamanager.entrecode.de/api/beefbeef/"), UUID.randomUUID());
        assertNotNull(dm);
        assertTrue(dm.getClass() == DataManager.class);
        assertNotNull(dm.getToken());
        assertTrue(!dm.getReadOnly());
    }

    @Test
    public void getAccessTokenOfDataManagerWithoutToken() throws ECMalformedDataManagerIDException {
        DataManager.create("beef1234", dataManager -> {
            assertNotNull(dataManager.getToken());
        });
    }


}