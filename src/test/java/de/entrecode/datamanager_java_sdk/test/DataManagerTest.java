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
import de.entrecode.datamanager_java_sdk.model.ECEntry;
import de.entrecode.datamanager_java_sdk.model.ECError;
import de.entrecode.datamanager_java_sdk.model.ECList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
                        case "/4e430eb2-aaaa-4f76-9f68-b4b4bacdc7dd/url":
                        case "/4e430eb2-aaaa-4f76-9f68-b4b4bacdc7dd/url?nocrop=true":
                        case "/4e430eb2-aaaa-4f76-9f68-b4b4bacdc7dd/url?nocrop=false&size=200":
                            return new MockResponse().setResponseCode(404);
                        case "/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url?nocrop=false&size=200":
                            return new MockResponse().setResponseCode(200).setBody("{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_200.jpg\"}");
                        case "/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url?nocrop=true&size=200":
                            return new MockResponse().setResponseCode(200).setBody("{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh_256.jpg\"}");
                        case "/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url":
                        case "/4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd/url?nocrop=true":
                            return new MockResponse().setResponseCode(200).setBody("{\"url\":\"https://ec-datamanager-default-bucket.s3.amazonaws.com//home/www/datamanagerfiles/c024f209/rcf4bgN0mQWl35PuQ8w08SVh.jpg\"}");
                        case "/api/beef1234/to-do-item-single?size=10&page=1":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":1,\"total\":1,\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234\"},\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/beef1234/{rel}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-single\"},\"beef1234:to-do-item-single/options\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item{?created,createdFrom,createdTo,done,id,modified,modifiedFrom,modifiedTo,page,private,size,sort,todo-text,todo-text~}\",\"templated\":true}},\"_embedded\":{\"beef1234:to-do-item-single\":{\"id\":\"VJY4n7vcI\",\"created\":\"2015-06-17T13:22:27.404Z\",\"modified\":\"2015-06-17T13:22:27.404Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-single\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-single?id=VJY4n7vcI\"},\"beef1234:to-do-item/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}}}");
                        case "/api/beef1234/to-do-item-multiple?size=10&page=1":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":10,\"total\":116,\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234\"},\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/beef1234/{rel}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"next\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?page=2\"},\"beef1234:to-do-item-multiple/options\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple{?created,createdFrom,createdTo,done,id,modified,modifiedFrom,modifiedTo,page,private,size,sort,todo-text,todo-text~}\",\"templated\":true}},\"_embedded\":{\"beef1234:to-do-item-multiple\":[{\"id\":\"VJY4n7vcI\",\"created\":\"2015-06-17T13:22:27.404Z\",\"modified\":\"2015-06-17T13:22:27.404Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=VJY4n7vcI\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"4kmswCbFI\",\"created\":\"2015-06-16T13:06:19.043Z\",\"modified\":\"2015-06-17T13:42:05.844Z\",\"private\":false,\"done\":true,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=4kmswCbFI\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"NymKWRWYU\",\"created\":\"2015-06-16T13:04:40.518Z\",\"modified\":\"2015-06-16T13:04:40.518Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=NymKWRWYU\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"4km5hpZKU\",\"created\":\"2015-06-16T13:03:21.270Z\",\"modified\":\"2015-06-16T13:03:21.270Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=4km5hpZKU\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"VymVVa-tL\",\"created\":\"2015-06-16T13:01:07.421Z\",\"modified\":\"2015-06-16T13:01:07.421Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=VymVVa-tL\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"4yXRz6ZtL\",\"created\":\"2015-06-16T13:00:45.753Z\",\"modified\":\"2015-06-16T13:00:45.753Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=4yXRz6ZtL\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"E1lu53WFI\",\"created\":\"2015-06-16T12:58:31.442Z\",\"modified\":\"2015-06-16T12:58:31.442Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=E1lu53WFI\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"V1lVYn-FI\",\"created\":\"2015-06-16T12:58:11.736Z\",\"modified\":\"2015-06-16T12:58:11.736Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=V1lVYn-FI\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"N1gRg2-t8\",\"created\":\"2015-06-16T12:55:57.946Z\",\"modified\":\"2015-06-16T12:55:57.946Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=N1gRg2-t8\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}},{\"id\":\"V1nl2ZtU\",\"created\":\"2015-06-16T12:55:55.138Z\",\"modified\":\"2015-06-16T12:55:55.138Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-multiple?id=V1nl2ZtU\"},\"beef1234:to-do-item-multiple/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}]}}");
                        case "/api/beef1234/to-do-item-none?size=10&page=1":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":0,\"total\":0,\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234\"},\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/beef1234/{rel}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-none\"},\"beef1234:to-do-item-none/options\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item-none{?created,createdFrom,createdTo,done,id,modified,modifiedFrom,modifiedTo,page,private,size,sort,todo-text,todo-text~}\",\"templated\":true}}}");
                        case "/api/beef1234/to-do-item?id=VJY4n7vcI":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":1,\"total\":1,\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234\"},\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/beef1234/{rel}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item?id=VJY4n7vcI\"},\"beef1234:to-do-item/options\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item{?created,createdFrom,createdTo,done,id,modified,modifiedFrom,modifiedTo,page,private,size,sort,todo-text,todo-text~}\",\"templated\":true}},\"_embedded\":{\"beef1234:to-do-item\":{\"id\":\"VJY4n7vcI\",\"created\":\"2015-06-17T13:22:27.404Z\",\"modified\":\"2015-06-17T13:22:27.404Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item?id=VJY4n7vcI\"},\"beef1234:to-do-item/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}}}");
                        // DM with 2 models
                        case "/api/beef1234/":
                            return new MockResponse().setResponseCode(200).setBody("{\"dataManagerID\":\"a50ae357-91b5-4d56-ba0e-7ec476f6c07d\",\"_links\":{\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/beef1234/relation/{rel}\",\"templated\":true},\"beef1234:to-do-item\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item\",\"name\":\"to-do-item list\"},\"beef1234:user\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user\",\"name\":\"user list\"},\"ec:api/assets\":{\"href\":\"https://datamanager.entrecode.de/asset/beef1234\"}}}");
                        // DM with user model
                        case "/api/beefbeef/":
                            return new MockResponse().setResponseCode(200).setBody("{\"dataManagerID\":\"a50ae357-91b5-4d56-ba0e-7ec476f6c07d\",\"_links\":{\"curies\":{\"name\":\"beefbeef\",\"href\":\"https://datamanager.entrecode.de/api/doc/beefbeef/relation/{rel}\",\"templated\":true},\"beefbeef:user\":{\"href\":\"https://datamanager.entrecode.de/api/beefbeef/user\",\"name\":\"user list\"},\"ec:api/assets\":{\"href\":\"https://datamanager.entrecode.de/asset/beefbeef\"}}}");
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
                            return new MockResponse().setResponseCode(404).setBody("{\"status\":404,\"code\":2100,\"title\":\"Resource not found\",\"type\":\"https://entrecode.de/doc/error/2100\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://entrecode.de/doc/error/2100\"}},\"detail\":\"\",\"verbose\":\"\"}");
                        case "/api/beef1234/to-do-item?id=AAAAAAAA":
                            return new MockResponse().setResponseCode(404).setBody("{\"status\":404,\"code\":2102,\"title\":\"No resource entity matching query string filter found\",\"type\":\"https://entrecode.de/doc/error/2102\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://entrecode.de/doc/error/2102\"}},\"detail\":\"\",\"verbose\":\"\"}");
                        default:
                            throw new RuntimeException("Not Mocked: " + request);
                    }
                case "PUT":
                    switch (request.getPath()) {
                        case "/api/beef1234/to-do-item?id=VJY4n7vcI":
                            return new MockResponse().setResponseCode(200).setBody("{\"count\":1,\"total\":1,\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234\"},\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/beef1234/{rel}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item?id=VJY4n7vcI\"},\"beef1234:to-do-item/options\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/to-do-item{?created,createdFrom,createdTo,done,id,modified,modifiedFrom,modifiedTo,page,private,size,sort,todo-text,todo-text~}\",\"templated\":true}},\"_embedded\":{\"beef1234:to-do-item\":{\"id\":\"VJY4n7vcI\",\"created\":\"2015-06-17T13:22:27.404Z\",\"modified\":\"2015-06-25T12:41:14.929Z\",\"private\":false,\"done\":true,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item?id=VJY4n7vcI\"},\"f84710b8:to-do-item/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}}}");
                        case "/api/beef1234/to-do-item?id=VJY4n7vcA":
                            return new MockResponse().setResponseCode(400).setBody("{\"status\":400,\"code\":2211,\"title\":\"Invalid format for property in JSON body\",\"type\":\"https://entrecode.de/doc/error/2211\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://entrecode.de/doc/error/2211\"}},\"detail\":\"Invalid type: string (expected boolean/null)\",\"verbose\":\"/done\"}");
                        case "/api/beef1234/to-do-item?id=VJY4n7vcZ":
                            return new MockResponse().setResponseCode(404).setBody("{\"status\":404,\"code\":2102,\"title\":\"No resource entity matching query string filter found\",\"type\":\"https://entrecode.de/doc/error/2102\",\"_links\":{\"up\":{\"title\":\"Data Manager Home Page\",\"href\":\"https://datamanager.entrecode.de\"},\"describedby\":{\"title\":\"Error Description\",\"href\":\"https://entrecode.de/doc/error/2102\"}},\"detail\":\"\",\"verbose\":\"\"}");
                        default:
                            throw new RuntimeException("Not Mocked: " + request);
                    }
                case "POST":
                    switch (request.getPath()) {
                        case "/api/beef1234/user":
                            return new MockResponse().setResponseCode(201).setBody("{\"count\":1,\"total\":1,\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8\"},\"curies\":{\"name\":\"f84710b8\",\"href\":\"https://datamanager.entrecode.de/api/doc/f84710b8/{rel}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/user\"},\"f84710b8:user/options\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/user{?created,createdFrom,createdTo,id,modified,modifiedFrom,modifiedTo,page,private,size,sort,temporaryToken,temporaryToken~}\",\"templated\":true}},\"_embedded\":{\"f84710b8:user\":{\"id\":\"EkWjwR8zv\",\"created\":\"2015-06-23T14:38:03.102Z\",\"modified\":\"2015-06-23T14:38:03.102Z\",\"private\":true,\"temporaryToken\":\"5c4ad68e-d03e-4476-92b3-1f0ae06c162e\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/user\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/user?id=EkWjwR8zv\"},\"f84710b8:user/creator\":{\"href\":\"https://datamanager.entrecode.de/api/f84710b8/user?id=EkWjwR8zv\"}}}}}");
                        case "/api/beef1234/to-do-item":
                            return new MockResponse().setResponseCode(201).setBody("{\"count\":1,\"total\":1,\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234\"},\"curies\":{\"name\":\"beef1234\",\"href\":\"https://datamanager.entrecode.de/api/doc/beef1234/{rel}\",\"templated\":true},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item?id=VJY4n7vcZ\"},\"beef1234:to-do-item/options\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item{?created,createdFrom,createdTo,done,id,modified,modifiedFrom,modifiedTo,page,private,size,sort,todo-text,todo-text~}\",\"templated\":true}},\"_embedded\":{\"beef1234:to-do-item\":{\"id\":\"NyxdCs1rw\",\"created\":\"2015-06-25T13:06:00.056Z\",\"modified\":\"2015-06-25T13:06:00.056Z\",\"private\":false,\"done\":false,\"todo-text\":\"Test text\",\"_links\":{\"collection\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item\"},\"self\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/to-do-item?id=NyxdCs1rw\"},\"beef1234:to-do-item/creator\":{\"href\":\"https://datamanager.entrecode.de/api/beef1234/user?id=QkHCflm2\"}}}}}");
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
        DataManager dm = new DataManager("id", UUID.randomUUID());
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
        DataManager dm = new DataManager("beefbeef", false);
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
    public void modelListMultiple() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);

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
    }

    @Test
    public void modelListDMNonexistent() throws IOException {
        DataManager dm = new DataManager(server.getUrl("/api/feedfeed"), true);

        final ECError[] error = new ECError[1];
        dm.modelList().onResponse(r -> fail()).onError(e -> error[0] = e).go();

        await().until(() -> error[0] != null);
        assertEquals(404, error[0].getStatus());
        assertEquals(2100, error[0].getCode());
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
        final boolean[] response = new boolean[1];
        DataManager dm = new DataManager(baseUrl, true);
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
        final boolean[] response = new boolean[1];
        DataManager dm = new DataManager(baseUrl, true);
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
    public void schemaGET() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);

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

        JsonObject[] schema = new JsonObject[1];
        dm.model("to-do-item").getSchema().forMethod("notsupported").onResponse(r -> schema[0] = r).onError(e -> fail(e.stringify())).go();
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
    public void getThumb() throws IOException {
        DataManager dm = new DataManager(baseUrl, true);
        String[] url = new String[1];
        dm.getImageThumbURL("4e430eb2-77e7-4f76-9f68-b4b4bacdc7dd").size(200).onResponse(r -> url[0] = r).onError(e -> fail(e.stringify())).go();

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
    public void assetsOne() throws IOException {
        fail();
    }

    @Test
    public void assetsNone() throws IOException {
        fail();
    }

    @Test
    public void assetsDMNonexistent() throws IOException {
        fail();
    }

    @Test
    public void asset() throws IOException {
        fail();
    }

    @Test
    public void assetNonexistent() throws IOException {
        fail();
    }

    @Test
    public void assetDelete() throws IOException {
        fail();
    }

    @Test
    public void assetDeleteNonexistent() throws IOException {
        // Response is ok
        fail();
    }

    @Test(expected = ECDataMangerInReadOnlyModeException.class)
    public void assetDeleteReadOnlyDM() throws IOException {
        fail();
    }

    @Test
    public void assetCreate() throws IOException {
        fail();
    }

    @Test
    public void assetCreateUnsupported() throws IOException {
        fail();
    }

    @Test
    public void assetCreateMultiple() throws IOException {
        fail();
    }

    @Test
    public void assetCreateMultipleUnsupported() throws IOException {
        fail();
    }

    @Test(expected = ECDataMangerInReadOnlyModeException.class)
    public void assetCreateReadOnlyDM() throws IOException {
        fail();
    }

    // TODO pagination (size, page)
    // TODO filter (allowed, not allowed)
    // TODO filter (to, from)
    // TODO filter (like/search)
}