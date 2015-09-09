package de.entrecode.datamanager_java_sdk.test;

import com.google.gson.JsonObject;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import de.entrecode.datamanager_java_sdk.DataManager;
import de.entrecode.datamanager_java_sdk.Model;
import de.entrecode.datamanager_java_sdk.exceptions.ECDataMangerInReadOnlyModeException;
import de.entrecode.datamanager_java_sdk.exceptions.ECMalformedDataManagerIDException;
import de.entrecode.datamanager_java_sdk.model.ECAsset;
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.model.ECError;
import de.entrecode.datamanager_java_sdk.model.ECList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.jayway.awaitility.Awaitility.await;
import static org.junit.Assert.*;

/**
 * Created by Simon Scherzinger, entrecode GmbH, Stuttgart (Germany), entrecode GmbH, Stuttgart (Germany) on 25.05.15.
 */
public class DataManagerTest {
    private MockWebServer server;
    private URL baseUrl;
    private Dispatcher dispatcher = new Dispatcher() {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
            switch (request.getMethod()) {
                case "GET":
                    switch (request.getPath()) {
                        case "/api/beef1234/user?id=QkHCflm2":
                            return new MockResponse().setResponseCode(200).setBody("{\"id\":\"QkHCflm2\",\"created\":\"2015-04-17T07:46:24.908Z\",\"modified\":\"2015-04-17T07:46:24.908Z\",\"private\":true,\"temporaryToken\":\"e63dca99-6a56-43a5-8864-1a63ee8565e7\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"},\"f84710b8:user/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}");
                        case "/asset/beef1234?assetID=4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd":
                            return new MockResponse().setResponseCode(200).setBody("{\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\",\"created\":\"2015-06-17T11:35:52.560Z\",\"files\":[{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh.jpg\",\"mimetype\":\"image/jpeg\",\"size\":395285,\"resolution\":{\"width\":1080,\"height\":1920},\"locale\":null,\"created\":\"2015-06-17T13:35:52.553+02:00\",\"modified\":\"2015-06-17T13:35:52.553+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_256.jpg\",\"mimetype\":\"image/jpeg\",\"size\":7653,\"resolution\":{\"width\":144,\"height\":256},\"locale\":null,\"created\":\"2015-06-17T13:35:52.734+02:00\",\"modified\":\"2015-06-17T13:35:52.734+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_50.jpg\",\"mimetype\":\"image/jpeg\",\"size\":1992,\"resolution\":{\"width\":50,\"height\":50},\"locale\":null,\"created\":\"2015-06-17T13:35:52.739+02:00\",\"modified\":\"2015-06-17T13:35:52.739+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_1024.jpg\",\"mimetype\":\"image/jpeg\",\"size\":59724,\"resolution\":{\"width\":576,\"height\":1024},\"locale\":null,\"created\":\"2015-06-17T13:35:52.742+02:00\",\"modified\":\"2015-06-17T13:35:52.742+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_400.jpg\",\"mimetype\":\"image/jpeg\",\"size\":21261,\"resolution\":{\"width\":400,\"height\":400},\"locale\":null,\"created\":\"2015-06-17T13:35:52.748+02:00\",\"modified\":\"2015-06-17T13:35:52.748+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_512.jpg\",\"mimetype\":\"image/jpeg\",\"size\":20270,\"resolution\":{\"width\":288,\"height\":512},\"locale\":null,\"created\":\"2015-06-17T13:35:52.75+02:00\",\"modified\":\"2015-06-17T13:35:52.75+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_200.jpg\",\"mimetype\":\"image/jpeg\",\"size\":8119,\"resolution\":{\"width\":200,\"height\":200},\"locale\":null,\"created\":\"2015-06-17T13:35:52.752+02:00\",\"modified\":\"2015-06-17T13:35:52.752+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_100.jpg\",\"mimetype\":\"image/jpeg\",\"size\":3676,\"resolution\":{\"width\":100,\"height\":100},\"locale\":null,\"created\":\"2015-06-17T13:35:52.765+02:00\",\"modified\":\"2015-06-17T13:35:52.765+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"}],\"tags\":[],\"title\":\"test\",\"type\":\"image\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234\"},\"ec:asset/best-file\":{\"href\":\"https://f.angus.entrecode.de/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},\"self\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234?assetID=4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\",\"title\":\"test\"},\"ec:api/asset/creator\":{\"href\":\"https://datamanager.angus.entrecode.de/api/beef1234/user?id=V1xSU1EqU\"},\"curies\":{\"href\":\"https://angus.entrecode.de/doc/rel/{rel}\",\"templated\":true}}}");
                        case "/asset/beef1234?size=10&page=1":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":1,\"total\":1,\"_links\":{\"curies\":{\"href\":\"https://angus.entrecode.de/doc/rel/{rel}\",\"templated\":true},\"ec:api/asset/by-id\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beeffeed{?assetID}\",\"templated\":true},\"ec:api\":{\"href\":\"https://datamanager.angus.entrecode.de/api/beeffeed\"},\"ec:assets/options\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beeffeed{?assetID,created,createdFrom,createdTo,created~,creator,dataManagerID,page,size,sort,tag,title,title~,type,type~}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beeffeed\"},\"next\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beeffeed?page=2\"}},\"_embedded\":{\"ec:api/asset\":{\"assetID\":\"3d1dcc0f-cb33-4c39-892b-1afbab9395b0\",\"created\":\"2015-06-17T10:13:10.190Z\",\"files\":[{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beeffeed/aYo_T7CFB0ylemZhcxrY_iwj.jpg\",\"mimetype\":\"image/jpeg\",\"size\":395285,\"resolution\":{\"width\":1080,\"height\":1920},\"locale\":null,\"created\":\"2015-06-17T12:13:10.118+02:00\",\"modified\":\"2015-06-17T12:13:10.118+02:00\",\"assetID\":\"3d1dcc0f-cb33-4c39-892b-1afbab9395b0\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beeffeed/aYo_T7CFB0ylemZhcxrY_iwj_200.jpg\",\"mimetype\":\"image/jpeg\",\"size\":8119,\"resolution\":{\"width\":200,\"height\":200},\"locale\":null,\"created\":\"2015-06-17T12:13:10.504+02:00\",\"modified\":\"2015-06-17T12:13:10.504+02:00\",\"assetID\":\"3d1dcc0f-cb33-4c39-892b-1afbab9395b0\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beeffeed/aYo_T7CFB0ylemZhcxrY_iwj_512.jpg\",\"mimetype\":\"image/jpeg\",\"size\":20270,\"resolution\":{\"width\":288,\"height\":512},\"locale\":null,\"created\":\"2015-06-17T12:13:10.507+02:00\",\"modified\":\"2015-06-17T12:13:10.507+02:00\",\"assetID\":\"3d1dcc0f-cb33-4c39-892b-1afbab9395b0\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beeffeed/aYo_T7CFB0ylemZhcxrY_iwj_400.jpg\",\"mimetype\":\"image/jpeg\",\"size\":21261,\"resolution\":{\"width\":400,\"height\":400},\"locale\":null,\"created\":\"2015-06-17T12:13:10.511+02:00\",\"modified\":\"2015-06-17T12:13:10.511+02:00\",\"assetID\":\"3d1dcc0f-cb33-4c39-892b-1afbab9395b0\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beeffeed/aYo_T7CFB0ylemZhcxrY_iwj_1024.jpg\",\"mimetype\":\"image/jpeg\",\"size\":59724,\"resolution\":{\"width\":576,\"height\":1024},\"locale\":null,\"created\":\"2015-06-17T12:13:10.515+02:00\",\"modified\":\"2015-06-17T12:13:10.515+02:00\",\"assetID\":\"3d1dcc0f-cb33-4c39-892b-1afbab9395b0\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beeffeed/aYo_T7CFB0ylemZhcxrY_iwj_256.jpg\",\"mimetype\":\"image/jpeg\",\"size\":7653,\"resolution\":{\"width\":144,\"height\":256},\"locale\":null,\"created\":\"2015-06-17T12:13:10.525+02:00\",\"modified\":\"2015-06-17T12:13:10.525+02:00\",\"assetID\":\"3d1dcc0f-cb33-4c39-892b-1afbab9395b0\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beeffeed/aYo_T7CFB0ylemZhcxrY_iwj_50.jpg\",\"mimetype\":\"image/jpeg\",\"size\":1992,\"resolution\":{\"width\":50,\"height\":50},\"locale\":null,\"created\":\"2015-06-17T12:13:10.525+02:00\",\"modified\":\"2015-06-17T12:13:10.525+02:00\",\"assetID\":\"3d1dcc0f-cb33-4c39-892b-1afbab9395b0\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beeffeed/aYo_T7CFB0ylemZhcxrY_iwj_100.jpg\",\"mimetype\":\"image/jpeg\",\"size\":3676,\"resolution\":{\"width\":100,\"height\":100},\"locale\":null,\"created\":\"2015-06-17T12:13:10.525+02:00\",\"modified\":\"2015-06-17T12:13:10.525+02:00\",\"assetID\":\"3d1dcc0f-cb33-4c39-892b-1afbab9395b0\"}],\"tags\":[],\"title\":\"test\",\"type\":\"image\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beeffeed\"},\"ec:asset/best-file\":{\"href\":\"https://f.angus.entrecode.de/3d1dcc0f-cb33-4c39-892b-1afbab9395b0\"},\"self\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beeffeed?assetID=3d1dcc0f-cb33-4c39-892b-1afbab9395b0\",\"title\":\"test\"},\"ec:api/asset/creator\":{\"href\":\"https://datamanager.angus.entrecode.de/api/beeffeed/user?id=V1xSU1EqU\"}}}}}");
                        case "/asset/beefbeef?size=10&page=1":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":2,\"total\":2,\"_links\":{\"curies\":{\"href\":\"https://angus.entrecode.de/doc/rel/{rel}\",\"templated\":true},\"ec:api/asset/by-id\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234{?assetID}\",\"templated\":true},\"ec:api\":{\"href\":\"https://datamanager.angus.entrecode.de/api/beef1234\"},\"ec:assets/options\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234{?assetID,created,createdFrom,createdTo,created~,creator,dataManagerID,page,size,sort,tag,title,title~,type,type~}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234\"},\"next\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234?page=2\"}},\"_embedded\":{\"ec:api/asset\":[{\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\",\"created\":\"2015-06-17T11:35:52.560Z\",\"files\":[{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/rcf4bgN0mQWl35PuQ8w08SVh.jpg\",\"mimetype\":\"image/jpeg\",\"size\":395285,\"resolution\":{\"width\":1080,\"height\":1920},\"locale\":null,\"created\":\"2015-06-17T13:35:52.553+02:00\",\"modified\":\"2015-06-17T13:35:52.553+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/rcf4bgN0mQWl35PuQ8w08SVh_256.jpg\",\"mimetype\":\"image/jpeg\",\"size\":7653,\"resolution\":{\"width\":144,\"height\":256},\"locale\":null,\"created\":\"2015-06-17T13:35:52.734+02:00\",\"modified\":\"2015-06-17T13:35:52.734+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/rcf4bgN0mQWl35PuQ8w08SVh_50.jpg\",\"mimetype\":\"image/jpeg\",\"size\":1992,\"resolution\":{\"width\":50,\"height\":50},\"locale\":null,\"created\":\"2015-06-17T13:35:52.739+02:00\",\"modified\":\"2015-06-17T13:35:52.739+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/rcf4bgN0mQWl35PuQ8w08SVh_1024.jpg\",\"mimetype\":\"image/jpeg\",\"size\":59724,\"resolution\":{\"width\":576,\"height\":1024},\"locale\":null,\"created\":\"2015-06-17T13:35:52.742+02:00\",\"modified\":\"2015-06-17T13:35:52.742+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/rcf4bgN0mQWl35PuQ8w08SVh_400.jpg\",\"mimetype\":\"image/jpeg\",\"size\":21261,\"resolution\":{\"width\":400,\"height\":400},\"locale\":null,\"created\":\"2015-06-17T13:35:52.748+02:00\",\"modified\":\"2015-06-17T13:35:52.748+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/rcf4bgN0mQWl35PuQ8w08SVh_512.jpg\",\"mimetype\":\"image/jpeg\",\"size\":20270,\"resolution\":{\"width\":288,\"height\":512},\"locale\":null,\"created\":\"2015-06-17T13:35:52.75+02:00\",\"modified\":\"2015-06-17T13:35:52.75+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/rcf4bgN0mQWl35PuQ8w08SVh_200.jpg\",\"mimetype\":\"image/jpeg\",\"size\":8119,\"resolution\":{\"width\":200,\"height\":200},\"locale\":null,\"created\":\"2015-06-17T13:35:52.752+02:00\",\"modified\":\"2015-06-17T13:35:52.752+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/rcf4bgN0mQWl35PuQ8w08SVh_100.jpg\",\"mimetype\":\"image/jpeg\",\"size\":3676,\"resolution\":{\"width\":100,\"height\":100},\"locale\":null,\"created\":\"2015-06-17T13:35:52.765+02:00\",\"modified\":\"2015-06-17T13:35:52.765+02:00\",\"assetID\":\"4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"}],\"tags\":[],\"title\":\"test\",\"type\":\"image\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234\"},\"ec:asset/best-file\":{\"href\":\"https://f.angus.entrecode.de/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\"},\"self\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234?assetID=4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd\",\"title\":\"test\"},\"ec:api/asset/creator\":{\"href\":\"https://datamanager.angus.entrecode.de/api/beef1234/user?id=V1xSU1EqU\"}}},{\"assetID\":\"5cf8274a-4eec-4705-a187-096529e41b08\",\"created\":\"2015-06-17T11:34:45.101Z\",\"files\":[{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/BIoX8vgGr8RW6aU3Kol7RRyE.jpg\",\"mimetype\":\"image/jpeg\",\"size\":395285,\"resolution\":{\"width\":1080,\"height\":1920},\"locale\":null,\"created\":\"2015-06-17T13:34:45.013+02:00\",\"modified\":\"2015-06-17T13:34:45.013+02:00\",\"assetID\":\"5cf8274a-4eec-4705-a187-096529e41b08\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/BIoX8vgGr8RW6aU3Kol7RRyE_400.jpg\",\"mimetype\":\"image/jpeg\",\"size\":21261,\"resolution\":{\"width\":400,\"height\":400},\"locale\":null,\"created\":\"2015-06-17T13:34:45.389+02:00\",\"modified\":\"2015-06-17T13:34:45.389+02:00\",\"assetID\":\"5cf8274a-4eec-4705-a187-096529e41b08\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/BIoX8vgGr8RW6aU3Kol7RRyE_512.jpg\",\"mimetype\":\"image/jpeg\",\"size\":20270,\"resolution\":{\"width\":288,\"height\":512},\"locale\":null,\"created\":\"2015-06-17T13:34:45.4+02:00\",\"modified\":\"2015-06-17T13:34:45.4+02:00\",\"assetID\":\"5cf8274a-4eec-4705-a187-096529e41b08\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/BIoX8vgGr8RW6aU3Kol7RRyE_256.jpg\",\"mimetype\":\"image/jpeg\",\"size\":7653,\"resolution\":{\"width\":144,\"height\":256},\"locale\":null,\"created\":\"2015-06-17T13:34:45.409+02:00\",\"modified\":\"2015-06-17T13:34:45.409+02:00\",\"assetID\":\"5cf8274a-4eec-4705-a187-096529e41b08\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/BIoX8vgGr8RW6aU3Kol7RRyE_200.jpg\",\"mimetype\":\"image/jpeg\",\"size\":8119,\"resolution\":{\"width\":200,\"height\":200},\"locale\":null,\"created\":\"2015-06-17T13:34:45.413+02:00\",\"modified\":\"2015-06-17T13:34:45.413+02:00\",\"assetID\":\"5cf8274a-4eec-4705-a187-096529e41b08\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/BIoX8vgGr8RW6aU3Kol7RRyE_1024.jpg\",\"mimetype\":\"image/jpeg\",\"size\":59724,\"resolution\":{\"width\":576,\"height\":1024},\"locale\":null,\"created\":\"2015-06-17T13:34:45.414+02:00\",\"modified\":\"2015-06-17T13:34:45.414+02:00\",\"assetID\":\"5cf8274a-4eec-4705-a187-096529e41b08\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/BIoX8vgGr8RW6aU3Kol7RRyE_50.jpg\",\"mimetype\":\"image/jpeg\",\"size\":1992,\"resolution\":{\"width\":50,\"height\":50},\"locale\":null,\"created\":\"2015-06-17T13:34:45.418+02:00\",\"modified\":\"2015-06-17T13:34:45.418+02:00\",\"assetID\":\"5cf8274a-4eec-4705-a187-096529e41b08\"},{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/beef1234/BIoX8vgGr8RW6aU3Kol7RRyE_100.jpg\",\"mimetype\":\"image/jpeg\",\"size\":3676,\"resolution\":{\"width\":100,\"height\":100},\"locale\":null,\"created\":\"2015-06-17T13:34:45.425+02:00\",\"modified\":\"2015-06-17T13:34:45.425+02:00\",\"assetID\":\"5cf8274a-4eec-4705-a187-096529e41b08\"}],\"tags\":[],\"title\":\"test\",\"type\":\"image\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234\"},\"ec:asset/best-file\":{\"href\":\"https://f.angus.entrecode.de/5cf8274a-4eec-4705-a187-096529e41b08\"},\"self\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234?assetID=5cf8274a-4eec-4705-a187-096529e41b08\",\"title\":\"test\"},\"ec:api/asset/creator\":{\"href\":\"https://datamanager.angus.entrecode.de/api/beef1234/user?id=V1xSU1EqU\"}}}]}}");
                        case "/asset/beeffeed?size=10&page=1":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":0,\"total\":0,\"_links\":{\"curies\":{\"href\":\"https://angus.entrecode.de/doc/rel/{rel}\",\"templated\":true},\"ec:api/asset/by-id\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234{?assetID}\",\"templated\":true},\"ec:api\":{\"href\":\"https://datamanager.angus.entrecode.de/api/beef1234\"},\"ec:assets/options\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234{?assetID,created,createdFrom,createdTo,created~,creator,dataManagerID,page,size,sort,tag,title,title~,type,type~}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234\"}}}");
                        case "/files/4e430eb2-aaaa-4f76-9f68-b4b4bacdc7dd/url":
                        case "/files/4e430eb2-aaaa-4f76-9f68-b4b4bacdc7dd/url?thumb=true&size=200":
                            return new MockResponse().setResponseCode(404);
                        case "/files/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url?thumb=true&size=50":
                        case "/files/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url?thumb=true&size=100":
                        case "/files/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url?thumb=true&size=200":
                        case "/files/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url?thumb=true&size=400":
                            return new MockResponse().setResponseCode(200).setBody("{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_200.jpg\"}");
                        case "/files/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url?size=200":
                            return new MockResponse().setResponseCode(200).setBody("{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_256.jpg\"}");
                        case "/files/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url":
                        case "/files/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url?thumb=false":
                            return new MockResponse().setResponseCode(200).setBody("{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh.jpg\"}");
                        case "/api/beef1234/to-do-item-single?size=10&page=1":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":1,\"total\":1,\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234\"},\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/beef1234/{rel}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-single\"},\"beef1234:to-do-item-single/options\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item{?created,createdFrom,createdTo,done,id,modified,modifiedFrom,modifiedTo,page,private,size,sort,todo-text,todo-text~}\",\"templated\":true}},\"_embedded\":{\"beef1234:to-do-item-single\":{\"id\":\"VJY4n7vcI\",\"created\":\"2015-06-17T13:22:27.404Z\",\"modified\":\"2015-06-17T13:22:27.404Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-single\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-single?id=VJY4n7vcI\"},\"beef1234:to-do-item/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}}}");
                        case "/api/beef1234/to-do-item-multiple?size=10&page=1":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":10,\"total\":116,\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234\"},\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/beef1234/{rel}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"next\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?page=2\"},\"beef1234:to-do-item-multiple/options\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple{?created,createdFrom,createdTo,done,id,modified,modifiedFrom,modifiedTo,page,private,size,sort,todo-text,todo-text~}\",\"templated\":true}},\"_embedded\":{\"beef1234:to-do-item-multiple\":[{\"id\":\"VJY4n7vcI\",\"created\":\"2015-06-17T13:22:27.404Z\",\"modified\":\"2015-06-17T13:22:27.404Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=VJY4n7vcI\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"4kmswCbFI\",\"created\":\"2015-06-16T13:06:19.043Z\",\"modified\":\"2015-06-17T13:42:05.844Z\",\"private\":false,\"done\":true,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=4kmswCbFI\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"NymKWRWYU\",\"created\":\"2015-06-16T13:04:40.518Z\",\"modified\":\"2015-06-16T13:04:40.518Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=NymKWRWYU\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"4km5hpZKU\",\"created\":\"2015-06-16T13:03:21.270Z\",\"modified\":\"2015-06-16T13:03:21.270Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=4km5hpZKU\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"VymVVa-tL\",\"created\":\"2015-06-16T13:01:07.421Z\",\"modified\":\"2015-06-16T13:01:07.421Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=VymVVa-tL\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"4yXRz6ZtL\",\"created\":\"2015-06-16T13:00:45.753Z\",\"modified\":\"2015-06-16T13:00:45.753Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=4yXRz6ZtL\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"E1lu53WFI\",\"created\":\"2015-06-16T12:58:31.442Z\",\"modified\":\"2015-06-16T12:58:31.442Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=E1lu53WFI\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"V1lVYn-FI\",\"created\":\"2015-06-16T12:58:11.736Z\",\"modified\":\"2015-06-16T12:58:11.736Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=V1lVYn-FI\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"N1gRg2-t8\",\"created\":\"2015-06-16T12:55:57.946Z\",\"modified\":\"2015-06-16T12:55:57.946Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=N1gRg2-t8\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"V1nl2ZtU\",\"created\":\"2015-06-16T12:55:55.138Z\",\"modified\":\"2015-06-16T12:55:55.138Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=V1nl2ZtU\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}]}}");
                        case "/api/beef1234/to-do-item-none?size=10&page=1":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":0,\"total\":0,\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234\"},\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/beef1234/{rel}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-none\"},\"beef1234:to-do-item-none/options\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-none{?created,createdFrom,createdTo,done,id,modified,modifiedFrom,modifiedTo,page,private,size,sort,todo-text,todo-text~}\",\"templated\":true}}}");
                        case "/api/beef1234/to-do-item?id=VJY4n7vcI":
                            return new MockResponse().setResponseCode(200).setBody("{\"id\":\"VJY4n7vcI\",\"created\":\"2015-06-17T13:22:27.404Z\",\"modified\":\"2015-06-17T13:22:27.404Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item?id=VJY4n7vcI\"},\"beef1234:to-do-item/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}");
                        // DM with 2 models
                        case "/api/beef1234/":
                            return new MockResponse().setResponseCode(200).setBody("{\"_links\":{\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/f84710b8/relation/{rel}\",\"templated\":true}},\"_embedded\":{\"beef1234:to-do-item\":{\"hexColor\":\"#abcdef\",\"titleField\":\"to-do-text\",\"_links\":{\"self\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/to-do-item\"}}},\"beef1234:user\":{\"hexColor\":\"#abcdef\",\"titleField\":\"id\",\"_links\":{\"self\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/user\"}}}}}");
                        // DM with user model
                        case "/api/beefbeef/":
                            return new MockResponse().setResponseCode(200).setBody("{\"_links\":{\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/f84710b8/relation/{rel}\",\"templated\":true}},\"_embedded\":{\"beef1234:user\":{\"hexColor\":\"#abcdef\",\"titleField\":\"id\",\"_links\":{\"self\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/user\"}}}}}");
                        case "/api/schema/beef1234/to-do-item?template=get":
                        case "/api/schema/beef1234/to-do-item?template=put":
                        case "/api/schema/beef1234/to-do-item?template=post":
                            return new MockResponse().setResponseCode(200).setBody("{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"id\":\"https://datamanager.entrecode.de/api/schema/f84710b8/to-do-item\",\"type\":\"object\",\"allOf\":[{\"$ref\":\"https://entrecode.de/schema/hal#definitions/halResource\"},{\"additionalProperties\":false,\"properties\":{\"_links\":{\"$ref\":\"https://entrecode.de/schema/hal#_links\"},\"id\":{\"type\":\"string\",\"pattern\":\"^[0-9A-Za-z-_]{7,14}$\",\"description\":\"Unique identifier for this entry.\"},\"private\":{\"type\":\"boolean\",\"description\":\"Indicates if an entry was posted private.\"},\"created\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"Timestamp of the creation of this entry.\"},\"creator\":{\"type\":[\"string\",\"null\"],\"description\":\"Creator of this entry.\"},\"modified\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"Timestamp of the last modification of this entry.\"},\"todo-text\":{\"type\":\"string\",\"description\":\"The main description of the to do list item\"},\"done\":{\"type\":[\"boolean\",\"null\"],\"description\":\"Indicates if the item is done\"}},\"required\":[\"id\",\"created\",\"modified\",\"todo-text\"]}]}");
                        // Error 404; 2100
                        case "/api/beef1234/doesnotexist?size=10&page=1":
                        case "/api/beef1234/doesnotexist?id=VJY4n7vcl":
                        case "/api/feedfeed/":
                        case "/api/feedfeed/user?size=10&page=1":
                        case "/api/schema/beef1234/nonexistent?template=get":
                        case "/api/beefbeef/user?id=VJY4n7vcl":
                        case "/asset/feedbeef?size=10&page=1":
                            return new MockResponse().setResponseCode(404).setBody("{\"status\":404,\"code\":2100,\"title\":\"Resource not found\",\"type\":\"https://entrecode.de/doc/error/2100\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://entrecode.de/doc/error/2100\"}},\"detail\":\"\",\"verbose\":\"\"}");
                        case "/api/beef1234/to-do-item?id=AAAAAAAA":
                        case "/asset/beef1234?assetID=555ebdc3-fb84-42ba-b381-3345fb6f6132":
                            return new MockResponse().setResponseCode(404).setBody("{\"status\":404,\"code\":2102,\"title\":\"No resource entity matching query string filter found\",\"type\":\"https://entrecode.de/doc/error/2102\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://entrecode.de/doc/error/2102\"}},\"detail\":\"\",\"verbose\":\"\"}");
                        default:
                            throw new RuntimeException("Not Mocked: " + request);
                    }
                case "PUT":
                    switch (request.getPath()) {
                        case "/api/beef1234/to-do-item?id=VJY4n7vcI":
                            return new MockResponse().setResponseCode(200).setBody("{\"id\":\"VJY4n7vcI\",\"created\":\"2015-06-17T13:22:27.404Z\",\"modified\":\"2015-06-25T12:41:14.929Z\",\"private\":false,\"done\":true,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item?id=VJY4n7vcI\"},\"f84710b8:to-do-item/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}");
                        case "/api/beef1234/to-do-item?id=VJY4n7vcA":
                            return new MockResponse().setResponseCode(400).setBody("{\"status\":400,\"code\":2211,\"title\":\"Invalid format for property in JSON body\",\"type\":\"https://entrecode.de/doc/error/2211\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://entrecode.de/doc/error/2211\"}},\"detail\":\"Invalid type: string (expected boolean/null)\",\"verbose\":\"/done\"}");
                        case "/api/beef1234/to-do-item?id=VJY4n7vcZ":
                            return new MockResponse().setResponseCode(404).setBody("{\"status\":404,\"code\":2102,\"title\":\"No resource entity matching query string filter found\",\"type\":\"https://entrecode.de/doc/error/2102\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://entrecode.de/doc/error/2102\"}},\"detail\":\"\",\"verbose\":\"\"}");
                        default:
                            throw new RuntimeException("Not Mocked: " + request);
                    }
                case "POST":
                    switch (request.getPath()) {
                        case "/asset/beeffeed":
                            return new MockResponse().setResponseCode(400).setBody("{\"status\":400,\"code\":2213,\"title\":\"Invalid file format in upload\",\"type\":\"https://angus.entrecode.de/doc/error/2213\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.angus.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://angus.entrecode.de/doc/error/2213\"}},\"detail\":\"application/zip\",\"verbose\":\"\"}");
                        case "/asset/beef1234":
                            return new MockResponse().setResponseCode(201).setBody("{\"count\":1,\"total\":1,\"_links\":{\"ec:asset\":{\"href\":\"https://datamanager.angus.entrecode.de/asset/beef1234?assetID=0c41955e-878a-4c30-8cb1-b54241c544b0\",\"title\":\"icon\"}}}");
                        case "/asset/beefbeef":
                            return new MockResponse().setResponseCode(201).setBody("{\"count\":2,\"total\":2,\"_links\":{\"ec:asset\":[{\"href\":\"https://datamanager.angus.entrecode.de/asset/beefbeef?assetID=681923d1-07a3-4c29-9c09-ea88ae8eaca3\",\"title\":\"icon\"},{\"href\":\"https://datamanager.angus.entrecode.de/asset/beefbeef?assetID=d56cfd24-d38f-41f1-a226-9c49b813c582\",\"title\":\"logo\"}]}}");
                        case "/api/beef1234/user":
                            return new MockResponse().setResponseCode(201).setBody("{\"id\":\"EkWjwR8zv\",\"created\":\"2015-06-23T14:38:03.102Z\",\"modified\":\"2015-06-23T14:38:03.102Z\",\"private\":true,\"temporaryToken\":\"5c4ad68e-d03e-4476-92b3-1f0ae06c162e\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/user\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/user?id=EkWjwR8zv\"},\"f84710b8:user/creator\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/user?id=EkWjwR8zv\"}}}");
                        case "/api/beef1234/to-do-item":
                            return new MockResponse().setResponseCode(201).setBody("{\"id\":\"NyxdCs1rw\",\"created\":\"2015-06-25T13:06:00.056Z\",\"modified\":\"2015-06-25T13:06:00.056Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item?id=NyxdCs1rw\"},\"beef1234:to-do-item/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}");
                        case "/api/beef1234/to-do-item-fail":
                            return new MockResponse().setResponseCode(400).setBody("{\"status\":400,\"code\":2211,\"title\":\"Invalid format for property in JSON body\",\"type\":\"https://entrecode.de/doc/error/2211\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://entrecode.de/doc/error/2211\"}},\"detail\":\"Invalid type: string (expected boolean/null)\",\"verbose\":\"/done\"}");
                        case "/api/beef1234/doesnotexist":
                        case "/api/beefbeef/to-do-item":
                        case "/api/beefbeef/user":
                            return new MockResponse().setResponseCode(404).setBody("{\"status\":404,\"code\":2100,\"title\":\"Resource not found\",\"type\":\"https://entrecode.de/doc/error/2100\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://entrecode.de/doc/error/2100\"}},\"detail\":\"\",\"verbose\":\"\"}");
                        default:
                            throw new RuntimeException("Not Mocked: " + request);
                    }
                case "DELETE":
                    switch (request.getPath()) {
                        case "/api/beef1234/to-do-item?id=VJY4n7vcI":
                        case "/asset/beef1234assetID=555ebdc3-fb84-42ba-b381-3345fb6f6132":
                        case "/asset/beef1234assetID=555ebdc3-aaaa-42ba-b381-3345fb6f6132":
                            return new MockResponse().setResponseCode(204);
                        default:
                            throw new RuntimeException("Not Mocked: " + request);
                    }
                default:
                    throw new RuntimeException("Not Mocked: " + request);
            }
        }
    };

    @Before
    public void before() throws IOException {
        server = new MockWebServer();
        server.setDispatcher(dispatcher);
        server.start();
        baseUrl = server.getUrl("/api/beef1234");
    }

    @After
    public void after() throws IOException {
        server.shutdown();
    }

    @Test(expected = ECMalformedDataManagerIDException.class)
    public void dmMalformedID() throws ECMalformedDataManagerIDException {
        new DataManager("id", UUID.randomUUID());
    }

    @Test
    public void dmID() throws ECMalformedDataManagerIDException {
        DataManager dm = new DataManager("beef1234", UUID.randomUUID());
        assertNotNull(dm);
        assertTrue(dm.getClass() == DataManager.class);
        assertNotNull(dm.getToken());
        assertTrue(!dm.getReadOnly());
        assertNotNull(dm.getUrl());
    }

    @Test
    public void dmIDReadOnly() throws ECMalformedDataManagerIDException {
        DataManager dm = new DataManager("beefbeef", true);
        assertNotNull(dm);
        assertTrue(dm.getClass() == DataManager.class);
        assertTrue(dm.getReadOnly());
        assertNotNull(dm.getUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void dmIDReadOnlyFalse() throws ECMalformedDataManagerIDException {
        new DataManager("beefbeef", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dmUrlReadOnlyFalse() throws MalformedURLException {
        new DataManager(new URL("https://datamanager.entrecode.de/api/beefbeef"), false);
    }

    @Test(expected = ECMalformedDataManagerIDException.class)
    public void dmMalformedIDReadOnly() throws ECMalformedDataManagerIDException {
        new DataManager("id", true);
    }

    @Test
    public void dmUrl() throws MalformedURLException {
        DataManager dm = new DataManager(new URL("https://datamanager.entrecode.de/api/beefbeef"), UUID.randomUUID());
        assertNotNull(dm);
        assertTrue(dm.getClass() == DataManager.class);
        assertNotNull(dm.getToken());
        assertTrue(!dm.getReadOnly());
        assertNotNull(dm.getID());
    }

    @Test
    public void dmUrlSlash() throws MalformedURLException {
        DataManager dm = new DataManager(new URL("https://datamanager.entrecode.de/api/beefbeef/"), UUID.randomUUID());
        assertNotNull(dm);
        assertTrue(dm.getClass() == DataManager.class);
        assertNotNull(dm.getToken());
        assertTrue(!dm.getReadOnly());
    }

    @Test(expected = MalformedURLException.class)
    public void dmMalformedUrl() throws MalformedURLException {
        new DataManager(new URL("thisisnotaurl"), UUID.randomUUID());
    }

    @Test
    public void dmUrlReadOnly() throws MalformedURLException {
        DataManager dm = new DataManager(new URL("https://datamanager.entrecode.de/api/beefbeef"), true);
        assertNotNull(dm);
        assertTrue(dm.getClass() == DataManager.class);
        assertTrue(dm.getReadOnly());
    }

    @Test
    public void dmIDRegister() throws Exception {
        final DataManager[] dm = new DataManager[1];
        DataManager.create(baseUrl, dataManager -> dm[0] = dataManager, e -> fail(e.stringify()));

        await().until(() -> dm[0] != null);
        assertTrue(dm[0].getToken() != null);
        assertTrue(!dm[0].getReadOnly());
    }

    @Test
    public void modelListMultipleDMReadOnly() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);

        final List[] models = new List[1];
        dm.modelList().onResponse(r -> models[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> models[0] != null);
        assertTrue(models[0].get(0) instanceof Model);
        assertEquals(2, models[0].size());
    }

    @Test
    public void modelListMultiple() throws IOException {
        DataManager dm = new DataManager(baseUrl, UUID.randomUUID());

        final List[] models = new List[1];
        dm.modelList().onResponse(r -> models[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> models[0] != null);
        assertTrue(models[0].get(0) instanceof Model);
        assertEquals(2, models[0].size());
    }

    @Test
    public void modelListOnlyUser() throws IOException {
        DataManager dm = new DataManager(server.getUrl("/api/beefbeef"), true);

        final List[] models = new List[1];
        dm.modelList().onResponse(r -> models[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> models[0] != null);
        assertTrue(models[0].get(0) instanceof Model);
        assertEquals(1, models[0].size());
        Model m = (Model) models[0].get(0);
        assertEquals("#abcdef", m.getHexColor());
        assertEquals("id", m.getTitleField());
    }

    @Test
    public void modelListDMNonexistent() throws IOException {
        DataManager dm = new DataManager(server.getUrl("/api/feedfeed"), true);

        final ECError[] error = new ECError[1];
        dm.modelList().onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2100, error[0].getCode());
        assertNotNull(error[0].stringify());
    }

    @Test
    public void entriesMany() throws IOException, ECMalformedDataManagerIDException {
        final ECList[] entries = new ECList[1];

        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item-multiple").entries().onResponse(r -> entries[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> entries[0] != null);
        assertEquals(entries[0].getCount(), 10);
        assertTrue(entries[0].getEmbedded().get(0) instanceof ECEntry);
        assertEquals(entries[0].getEmbedded().size(), 10);
    }

    @Test
    public void entriesOne() throws IOException {
        final ECList[] entries = new ECList[1];

        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item-single").entries().onResponse(r -> entries[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> entries[0] != null);
        assertEquals(1, entries[0].getCount());
        assertTrue(entries[0].getEmbedded().get(0) instanceof ECEntry);
        assertEquals(1, entries[0].getEmbedded().size());
    }

    @Test
    public void entriesNone() throws IOException {
        final ECList[] entries = new ECList[1];

        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item-none").entries().onResponse(r -> entries[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> entries[0] != null);
        assertEquals(0, entries[0].getCount());
        assertTrue(entries[0].getEmbedded() == null);
    }

    @Test
    public void entriesModelNonexistent() throws IOException {
        final ECError[] error = new ECError[1];
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("doesnotexist").entries().onResponse(r -> fail()).onError(err -> error[0] = err).go();

        await().until(() -> error[0] != null);
        assertTrue(error[0] != null);
        assertEquals(2100, error[0].getCode());
        assertEquals(404, error[0].getStatus());
    }

    @Test
    public void entriesDMNonexistent() throws IOException {
        DataManager dm = new DataManager(server.getUrl("/api/feedfeed"), true);

        final ECError[] error = new ECError[1];
        dm.model("user").entries().onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2100, error[0].getCode());
    }

    @Test
    public void entry() throws IOException {
        final ECEntry[] entry = new ECEntry[1];
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item").entry("VJY4n7vcI").onResponse(e -> entry[0] = e).onError(e -> fail(e.stringify())).go();

        await().until(() -> entry[0] != null);
        assertEquals("VJY4n7vcI", entry[0].get("id"));
        assertEquals("Test text", entry[0].get("todo-text"));
        assertEquals(false, entry[0].get("done"));
    }

    @Test
    public void entryNonexistent() throws IOException {
        final ECError[] error = new ECError[1];
        DataManager dm = new DataManager(baseUrl, true);
        dm.model("to-do-item").entry("AAAAAAAA").onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2102, error[0].getCode());
    }

    @Test
    public void entryModelNonexistent() throws IOException {
        final ECError[] error = new ECError[1];
        DataManager dm = new DataManager(baseUrl, true);
        dm.model("doesnotexist").entry("VJY4n7vcl").onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2100, error[0].getCode());
    }

    @Test
    public void entryDMNonexistent() throws IOException {
        final ECError[] error = new ECError[1];
        DataManager dm = new DataManager(server.getUrl("/api/beefbeef/"), true);
        dm.model("user").entry("VJY4n7vcl").onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2100, error[0].getCode());
    }

    @Test
    public void entryDelete() throws IOException {
        final boolean[] response = new boolean[1];
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item").entry("VJY4n7vcI").onResponse(e -> {
            JsonObject links = e.getLinks();
            JsonObject self = links.get("self").getAsJsonObject();
            self.addProperty("href", baseUrl + "/to-do-item?id=VJY4n7vcI");
            links.add("self", self);
            e.setLinks(links);
            e.delete().onResponse(r -> response[0] = r).onError(e2 -> fail(e2.stringify())).go();
        }).onError(e -> fail(e.stringify())).go();
        await().until(() -> response[0]);
        assertTrue(response[0]);
    }

    @Test(expected = ECDataMangerInReadOnlyModeException.class)
    public void entryDeleteReadOnlyDM() throws IOException {
        ECEntry entry = new ECEntry();
        entry.delete().onResponse(r -> fail()).onError(e -> fail(e.stringify()));
    }

    @Test
    public void entryUpdateSuccess() throws IOException {
        final ECEntry[] entry = new ECEntry[1];
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item").entry("VJY4n7vcI").onResponse(e -> {
            JsonObject links = e.getLinks();
            JsonObject self = links.get("self").getAsJsonObject();
            self.addProperty("href", baseUrl + "/to-do-item?id=VJY4n7vcI");
            links.add("self", self);
            e.setLinks(links);
            e.set("done", true);
            e.save().onResponse(r -> entry[0] = r).onError(e2 -> fail(e2.stringify())).go();
        }).onError(e -> fail(e.stringify())).go();
        await().until(() -> entry[0] != null);
        assertTrue((boolean) entry[0].get("done"));
    }

    @Test
    public void entryUpdateFail() throws IOException {
        ECError[] error = new ECError[1];
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item").entry("VJY4n7vcI").onResponse(e -> {
            JsonObject links = e.getLinks();
            JsonObject self = links.get("self").getAsJsonObject();
            self.addProperty("href", baseUrl + "/to-do-item?id=VJY4n7vcA");
            links.add("self", self);
            e.setLinks(links);
            e.set("done", "notaboolean");
            e.save().onResponse(r -> fail()).onError(e2 -> error[0] = e2).go();
        }).onError(e -> fail(e.stringify())).go();

        await().until(() -> error[0] != null);
        assertEquals(400, error[0].getStatus());
        assertEquals(2211, error[0].getCode());
    }

    @Test
    public void entryUpdateNonexistent() throws IOException {
        ECError[] error = new ECError[1];
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item").entry("VJY4n7vcI").onResponse(e -> {
            JsonObject links = e.getLinks();
            JsonObject self = links.get("self").getAsJsonObject();
            self.addProperty("href", baseUrl + "/to-do-item?id=VJY4n7vcZ");
            links.add("self", self);
            e.setLinks(links);
            e.set("done", false);
            e.save().onResponse(r -> fail()).onError(e2 -> error[0] = e2).go();
        }).onError(e -> fail(e.stringify())).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2102, error[0].getCode());
    }

    @Test(expected = ECDataMangerInReadOnlyModeException.class)
    public void entryUpdateReadOnlyDM() throws IOException {
        ECEntry entry = new ECEntry();
        entry.save().onResponse(r -> fail()).onError(e -> fail(e.stringify()));
    }

    @Test
    public void entryCreate() throws IOException {
        final ECEntry[] entry = new ECEntry[1];
        ECEntry entryIn = new ECEntry();
        entryIn.set("todo-text", "hello");
        entryIn.set("done", false);
        entryIn.set("isPrivate", false);
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item").createEntry(entryIn).onResponse(e -> entry[0] = e).onError(e -> fail(e.stringify())).go();

        await().until(() -> entry[0] != null);
        assertEquals("NyxdCs1rw", entry[0].get("id"));
        assertEquals("Test text", entry[0].get("todo-text"));
        assertEquals(false, entry[0].get("done"));
    }

    @Test
    public void entryCreateFail() throws IOException {
        ECError[] error = new ECError[1];
        ECEntry entry = new ECEntry();
        entry.set("todo-text", "hello");
        entry.set("done", false);
        entry.set("isPrivate", false);
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item-fail").createEntry(entry).onResponse(e -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(400, error[0].getStatus());
        assertEquals(2211, error[0].getCode());
    }

    @Test
    public void entryCreateModelNonexistent() throws IOException {
        ECError[] error = new ECError[1];
        ECEntry entry = new ECEntry();
        entry.set("todo-text", "hello");
        entry.set("done", false);
        entry.set("isPrivate", false);
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("doesnotexist").createEntry(entry).onResponse(e -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2100, error[0].getCode());
    }

    @Test
    public void entryCreateDMNonexistent() throws IOException {
        ECError[] error = new ECError[1];
        ECEntry entry = new ECEntry();
        entry.set("todo-text", "hello");
        entry.set("done", false);
        entry.set("isPrivate", false);
        DataManager dm = new DataManager(server.getUrl("/api/beefbeef"), UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        dm.model("to-do-item").createEntry(entry).onResponse(e -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2100, error[0].getCode());
    }

    @Test(expected = ECDataMangerInReadOnlyModeException.class)
    public void entryCreateDMReadOnly() throws IOException {
        ECEntry entryIn = new ECEntry();
        entryIn.set("todo-text", "hello");
        entryIn.set("done", false);
        entryIn.set("isPrivate", false);
        DataManager dm = new DataManager(baseUrl, true);
        dm.model("to-do-item").createEntry(entryIn).onResponse(e -> fail()).onError(e -> fail(e.stringify())).go();
    }

    @Test
    public void schemaGETDMReadOnly() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);

        JsonObject[] schema = new JsonObject[1];
        dm.model("to-do-item").getSchema().onResponse(r -> schema[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> schema[0] != null);
        assertTrue(schema[0].has("$schema"));
        assertTrue(schema[0].has("id"));
    }

    @Test
    public void schemaGET() throws IOException {
        DataManager dm = new DataManager(baseUrl, UUID.randomUUID());

        JsonObject[] schema = new JsonObject[1];
        dm.model("to-do-item").getSchema().onResponse(r -> schema[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> schema[0] != null);
        assertTrue(schema[0].has("$schema"));
        assertTrue(schema[0].has("id"));
    }

    @Test
    public void schemaPUT() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);

        JsonObject[] schema = new JsonObject[1];
        dm.model("to-do-item").getSchema().forMethod("put").onResponse(r -> schema[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> schema[0] != null);
        assertTrue(schema[0].has("$schema"));
        assertTrue(schema[0].has("id"));
    }

    @Test
    public void schemaPOST() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);

        JsonObject[] schema = new JsonObject[1];
        dm.model("to-do-item").getSchema().forMethod("POST").onResponse(r -> schema[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> schema[0] != null);
        assertTrue(schema[0].has("$schema"));
        assertTrue(schema[0].has("id"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void schemaInvalidMethod() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        dm.model("to-do-item").getSchema().forMethod("notsupported").onResponse(r -> fail()).onError(e -> fail(e.stringify())).go();
    }

    @Test
    public void schemaNonexistentModel() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);

        ECError[] error = new ECError[1];
        dm.model("nonexistent").getSchema().onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2100, error[0].getCode());
    }

    @Test
    public void userRegister() throws IOException {
        ECEntry[] user = new ECEntry[1];
        DataManager dm = new DataManager(baseUrl, true);
        dm.register().onResponse(r -> user[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> user[0] != null);
        assertTrue(user[0].get("temporaryToken") != null);
        assertEquals("5c4ad68e-d03e-4476-92b3-1f0ae06c162e", user[0].get("temporaryToken"));
    }

    @Test
    public void userGet() throws IOException {
        ECEntry[] user = new ECEntry[1];
        DataManager dm = new DataManager(baseUrl, true);
        dm.user("QkHCflm2").onResponse(r -> user[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> user[0] != null);
        assertTrue(user[0].get("temporaryToken") != null);
        assertEquals("e63dca99-6a56-43a5-8864-1a63ee8565e7", user[0].get("temporaryToken"));
    }

    @Test
    public void userRegisterWithModelCreate() throws IOException {
        ECEntry[] user = new ECEntry[1];
        DataManager dm = new DataManager(baseUrl, true);
        dm.model("user").createEntry(new ECEntry()).onResponse(r -> user[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> user[0] != null);
        assertTrue(user[0].get("temporaryToken") != null);
        assertEquals("5c4ad68e-d03e-4476-92b3-1f0ae06c162e", user[0].get("temporaryToken"));
    }

    @Test
    public void userRegisterDMNonexistent() throws IOException {
        ECError[] error = new ECError[1];
        DataManager dm = new DataManager(server.getUrl("/api/beefbeef"), true);
        dm.register().onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2100, error[0].getCode());
    }

    @Test
    public void getFile() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        String[] url = new String[1];
        dm.getFileURL("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").onResponse(r -> url[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> url[0] != null);
        assertEquals("https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh.jpg", url[0]);
    }

    @Test
    public void getFileLocale() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        String[] url = new String[1];
        dm.getFileURL("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").locale("de-DE").onResponse(r -> url[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> url[0] != null);
        assertEquals("https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh.jpg", url[0]);
    }

    @Test
    public void getFileNonexistent() throws IOException {
        ECError[] error = new ECError[1];
        DataManager dm = new DataManager(baseUrl, true);
        dm.getFileURL("4e430eb2-aaaa-4f76-9f68-b4b4bacdc7dd").onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
    }

    @Test
    public void getImage() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        String[] url = new String[1];
        dm.getImageURL("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").onResponse(r -> url[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> url[0] != null);
        assertEquals("https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh.jpg", url[0]);
    }

    @Test
    public void getImageSize() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        String[] url = new String[1];
        dm.getImageURL("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").size(200).onResponse(r -> url[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> url[0] != null);
        assertEquals("https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_256.jpg", url[0]);
    }

    @Test
    public void getImageNonexistent() throws IOException {
        ECError[] error = new ECError[1];
        DataManager dm = new DataManager(baseUrl, true);
        dm.getImageURL("4e430eb2-aaaa-4f76-9f68-b4b4bacdc7dd").onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
    }

    @Test
    public void getThumb50() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        String[] url = new String[1];
        dm.getImageThumbURL("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").size(50).onResponse(r -> url[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> url[0] != null);
        assertEquals("https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_200.jpg", url[0]);
    }

    @Test
    public void getThumb100() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        String[] url = new String[1];
        dm.getImageThumbURL("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").size(100).onResponse(r -> url[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> url[0] != null);
        assertEquals("https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_200.jpg", url[0]);
    }

    @Test
    public void getThumb200() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        String[] url = new String[1];
        dm.getImageThumbURL("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").size(200).onResponse(r -> url[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> url[0] != null);
        assertEquals("https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_200.jpg", url[0]);
    }

    @Test
    public void getThumb400() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        String[] url = new String[1];
        dm.getImageThumbURL("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").size(400).onResponse(r -> url[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> url[0] != null);
        assertEquals("https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_200.jpg", url[0]);
    }

    @Test
    public void getTumbNonexistent() throws IOException {
        ECError[] error = new ECError[1];
        DataManager dm = new DataManager(baseUrl, true);
        dm.getImageThumbURL("4e430eb2-aaaa-4f76-9f68-b4b4bacdc7dd").size(200).onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getThumbNoSize() {
        DataManager dm = new DataManager(baseUrl, true);
        dm.getImageThumbURL("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").onResponse(r -> fail()).onError(e -> fail(e.stringify())).go();
    }

    @Test
    public void assetsMultiple() throws IOException {
        DataManager dm = new DataManager(server.getUrl("/api/beefbeef"), true);
        final ECList[] assets = new ECList[1];
        dm.assets().onResponse(r -> assets[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> assets[0] != null);
        assertEquals(2, assets[0].getCount());
        assertEquals(2, assets[0].getTotal());
        assertTrue(assets[0].getEmbedded().get(1) instanceof ECAsset);
    }

    @Test
    public void assetsOneDMReadOnly() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        final ECList[] assets = new ECList[1];
        dm.assets().onResponse(r -> assets[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> assets[0] != null);
        assertEquals(1, assets[0].getCount());
        assertEquals(1, assets[0].getTotal());
        assertTrue(assets[0].getEmbedded().get(0) instanceof ECAsset);
    }

    @Test
    public void assetsOne() throws IOException {
        DataManager dm = new DataManager(baseUrl, UUID.randomUUID());
        final ECList[] assets = new ECList[1];
        dm.assets().onResponse(r -> assets[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> assets[0] != null);
        assertEquals(1, assets[0].getCount());
        assertEquals(1, assets[0].getTotal());
        assertTrue(assets[0].getEmbedded().get(0) instanceof ECAsset);
    }

    @Test
    public void assetsNone() throws IOException {
        DataManager dm = new DataManager(server.getUrl("/api/beeffeed"), true);
        final ECList[] assets = new ECList[1];
        dm.assets().onResponse(r -> assets[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> assets[0] != null);
        assertEquals(0, assets[0].getCount());
        assertEquals(0, assets[0].getTotal());
    }

    @Test
    public void assetsDMNonexistent() throws IOException {
        ECError[] error = new ECError[1];
        DataManager dm = new DataManager(server.getUrl("/api/feedbeef"), true);
        dm.assets().onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2100, error[0].getCode());
    }

    @Test
    public void assetDMReadOnly() throws IOException {
        final ECAsset[] asset = new ECAsset[1];
        DataManager dm = new DataManager(baseUrl, true);
        dm.asset("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").onResponse(r -> asset[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> asset[0] != null);
        assertEquals("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd", asset[0].getAssetID());
    }

    @Test
    public void asset() throws IOException {
        final ECAsset[] asset = new ECAsset[1];
        DataManager dm = new DataManager(baseUrl, UUID.randomUUID());
        dm.asset("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").onResponse(r -> asset[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> asset[0] != null);
        assertEquals("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd", asset[0].getAssetID());
    }

    @Test
    public void assetNonexistent() throws IOException {
        ECError[] error = new ECError[1];
        DataManager dm = new DataManager(server.getUrl("/api/beef1234"), true);
        dm.asset("555ebdc3-fb84-42ba-b381-3345fb6f6132").onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2102, error[0].getCode());
    }

    @Test
    public void assetDelete() throws IOException {
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        ECAsset asset = new ECAsset(dm, server.getUrl("/asset/beef1234") + "assetID=555ebdc3-fb84-42ba-b381-3345fb6f6132", "deleteme");
        final Boolean[] response = new Boolean[1];
        asset.delete().onResponse(r -> response[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> response[0]);
        assertTrue(response[0]);
    }

    @Test
    public void assetDeleteNonexistent() throws IOException {
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        ECAsset asset = new ECAsset(dm, server.getUrl("/asset/beef1234") + "assetID=555ebdc3-aaaa-42ba-b381-3345fb6f6132", "deleteme");
        final Boolean[] response = new Boolean[1];
        asset.delete().onResponse(r -> response[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> response[0]);
        assertTrue(response[0]);
    }

    @Test(expected = ECDataMangerInReadOnlyModeException.class)
    public void assetDeleteReadOnlyDM() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        ECAsset asset = new ECAsset(dm, server.getUrl("/asset/beef1234") + "assetID=555ebdc3-aaaa-42ba-b381-3345fb6f6132", "deleteme");
        asset.delete().onResponse(r -> fail()).onError(e -> fail(e.stringify())).go();
    }

    @Test
    public void assetCreate() throws IOException {
        ClassLoader cl = DataManagerTest.class.getClassLoader();
        File file = new File(cl.getResource("test.jpg").getFile());
        DataManager dm = new DataManager(baseUrl, UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        List<ECAsset>[] asset = new List[1];
        dm.createAsset(file).onResponse(r -> asset[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> asset[0] != null);
        assertEquals("icon", asset[0].get(0).getTitle());
    }

    @Test(expected = ECDataMangerInReadOnlyModeException.class)
    public void assetCreateDMReadOnly() throws IOException {
        ClassLoader cl = DataManagerTest.class.getClassLoader();
        File file = new File(cl.getResource("test.jpg").getFile());
        DataManager dm = new DataManager(baseUrl, true);
        dm.createAsset(file).onResponse(r -> fail()).onError(e -> fail(e.stringify())).go();
    }

    @Test
    public void assetCreateUnsupported() throws IOException {
        ClassLoader cl = DataManagerTest.class.getClassLoader();
        File file = new File(cl.getResource("test.jpg").getFile());
        DataManager dm = new DataManager(server.getUrl("/api/beeffeed"), UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        ECError[] error = new ECError[1];
        dm.createAsset(file).onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(400, error[0].getStatus());
        assertEquals(2213, error[0].getCode());
    }

    @Test
    public void assetCreateMultiple() throws IOException {
        ClassLoader cl = DataManagerTest.class.getClassLoader();
        File file = new File(cl.getResource("test.jpg").getFile());
        DataManager dm = new DataManager(server.getUrl("/api/beefbeef"), UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        List<ECAsset>[] asset = new List[1];
        dm.createAsset(new ArrayList<File>() {{
            add(file);
            add(file);
        }}).onResponse(r -> asset[0] = r).onError(e -> fail(e.stringify())).go();

        await().until(() -> asset[0] != null);
        assertEquals("logo", asset[0].get(1).getTitle());
    }

    @Test(expected = ECDataMangerInReadOnlyModeException.class)
    public void assetCreateMultipleDMReadOnly() throws IOException {
        ClassLoader cl = DataManagerTest.class.getClassLoader();
        File file = new File(cl.getResource("test.jpg").getFile());
        DataManager dm = new DataManager(server.getUrl("/api/beefbeef"), true);
        List<ECAsset>[] asset = new List[1];
        dm.createAsset(new ArrayList<File>() {{
            add(file);
            add(file);
        }}).onResponse(r -> fail()).onError(e -> fail(e.stringify())).go();
    }

    @Test
    public void assetCreateMultipleUnsupported() throws IOException {
        ClassLoader cl = DataManagerTest.class.getClassLoader();
        File file = new File(cl.getResource("test.jpg").getFile());
        DataManager dm = new DataManager(server.getUrl("/api/beeffeed"), UUID.fromString("5c4ad68e-d03e-4476-92b3-1f0ae06c162e"));
        ECError[] error = new ECError[1];
        dm.createAsset(new ArrayList<File>() {{
            add(file);
            add(file);
        }}).onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(400, error[0].getStatus());
        assertEquals(2213, error[0].getCode());
    }

    @Test(expected = ECDataMangerInReadOnlyModeException.class)
    public void assetCreateReadOnlyDM() throws IOException {
        ClassLoader cl = DataManagerTest.class.getClassLoader();
        File file = new File(cl.getResource("test.jpg").getFile());
        DataManager dm = new DataManager(baseUrl, true);
        dm.createAsset(file).onResponse(r -> fail()).onError(e -> fail(e.stringify())).go();
    }

    // TODO pagination (size, page)
    // TODO filter (allowed, not allowed)
    // TODO filter (to, from)
    // TODO filter (like/search)
}