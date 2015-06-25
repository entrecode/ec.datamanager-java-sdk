# ec.datamanager-java-sdk

Java SDK for [ec.datamanager](https://entrecode.de/datamanager). By entrecode.

Simply use the generated APIs of the ec.datamanager with Java.

The SDK is fully asyncron.

## Setup

Get via Maven:

```xml
<dependency>
  <groupId>de.entrecode</groupId>
  <artifactId>datamanager_java_sdk</artifactId>
  <version>0.2.0</version>
</dependency>
```

or with Gradle:

```java
compile 'de.entrecode:datamanager_java_sdk:0.2.0'
```

## Usage

See the running example in `srv/main/java/de.entrecode.datamanager_java_sdk.example`.

### Initialization
You need to connect to your Data Manager API using the `DataManager(…)` constructors.

Initializing dataManager with existing token:

```java
DataManager dm = new DataManager(
  new URL("https://datamanager.entrecode.de/api/beefbeef"),
  UUID.fromString("8c3b7b55-531f-4a03-b584-09fdef59cb0c")
);
```

Alternative:

```java
DataManager dm = new DataManager(
  "beefbeef",
  UUID.fromString("8c3b7b55-531f-4a03-b584-09fdef59cb0c")
);
```

Initializing without token (will be generated):

```java
DataManager dm;
DataManager.create(
  new URL("https://datamanager.entrecode.de/api/beefbeef",
  new ECResponseListener<DataManager>{
    @Override
    public void onResponse(DataManager dataManager){
    	dm = dataManager;
    }
  }, new ECErrorListener{
  	@Override
  	public void onError(ECError error){
  		System.out.println(error.stringify());
  	}
  });
```

Alternative without token:

```java
DataManager dm;
DataManager.create(
  "beefbeef",
  new ECResponseListener<DataManager>{
    @Override
    public void onResponse(DataManager dataManager){
    	dm = dataManager;
    }
  }, new ECErrorListener{
  	@Override
  	public void onError(ECError error){
  		System.out.println(error.stringify());
  	}
  });
```

Initializing with read-only mode:

```java
DataManager dm = new DataManager("beefbeef", true); // throws ECMalformedDataManagerIDException

//OR

DataManager dm2 = new DataManager(
  new URL("https://datamanager.entrecode.de/api/beefbeef",
  true);
```

### Get Entries

```java
dm.model("myModel").entries()
    .onResponse(new ECResponseListener<List<ECEntry>>{
		@Override
		public void onResponse(List<ECEntry> entries){
			// TODO something		
		}
	})
	.onError(new ECErrorListener{
		@Override
		public void onError(ECError error){
			// TODO something
		}
	})
	.filter(new HashMap<String, String>(){{
		put("propertyA~", "LikeThat");
		put("propertyB", "ExactlyThat");
		put("propertyCFrom", "FromThat");
		put("propertyCTo", "ToThat");
	}})
	.go();
```

### Get Entry
```java
dm.model("myModel").entry("alwoigei")
	.onResponse(new ECResponseListener<ECEntry>{
		@Override
		public void onResponse(ECEntry entry){
			// TODO something
		}
	}).go();
```

### Create Entry

```java
ECEntry ecEntry = new ECEntry(…);
…
dm.model("myModel").createEntry(ecEntry)
	.onResponse(new ECResponseListener<ECEntry>{
		@Override
		public void onResponse(ECEntry entry){
			// TODO something
		}
	}).onError(new ECResponseListener<ECEntry>{
		@Override
		public void onResponse(ECEntry entry){
			// TODO something		
		}
	}).go();
```

### Delete Entry
```java
ECEntry ecEntry;
…
ecEntry.delete()
	.onResponse(new ECResponseListener{
		@Override
		public void onResponse(){
			// TODO something
		}
	}
	.onError(new ECErrorListener{
		@Override
		public void onError(ECError error){
			// TODO something
		}
	}).go();
```

### Update Entry
```java
ECEntry ecEntry;
…
ecEntry.save()
	.onResponse(new ECResponseListener<ECEntry>{
		@Override
		public void onResponse(ECEntry entry){
			// TODO something
		}
	})
	.onError(new ECErrorListener{
		@Override
		public void onError(){
			// TODO something
		}
	}).go();
```

### Model List

```java
dm.modelList()
	.onResponse(new ECResponseListener<List<Model>>{
		@Override
		public void onResponse(List<Model> models){
			// TODO something
		}
	}).go();
```

### Get JSON Schema

```java
dm.model("myModel").getSchema()
	.forMethod("PUT")
	.onResponse(new ECResponseListener<JsonObject>{
		@Override
		public void onResponse(JsonObject schema){
			// TODO something
		}
	})
	.onError(new ECErrorListener{
		@Override
		public void onError(){
			// TODO something
		}
	}).go();
```

### User Managerment

```java
dm.register()… // is shorthand for
dm.model("user").createEntry(new ECEntry())
	.onResponse(new ECResponseListener<UUID>{
		@Override
		public void onResponse(ECEntry user){
			// TODO save token
			saveSomwhere(user.getProperty("temporaryToken"));
		}
	})
	.onError(new ECErrorListener{
		@Override
		public void onError(){
			// TODO something
		}
	}).go();
```

Full example of updating a user entry:

```java
ECErrorListener ecel = new ECErrorListener{
	@Override
	public void onError(ECError error){
		// TODO something
	}
}
dm.user("lsadklja")… // is shorthand for
dm.model("user").entry("lsadklja")
	.onResponse(new ECResponseListener<ECEntry>{
		@Override
		public void onResponse(ECEntry user){
			user.set("name", "André Code");
			user.save()
				.onResponse(new ECREsponseListener<ECEntry>{
					@Override
					public void onResponse(ECEntry user){
						// TODO something
					}
				}).onError(ecel).go();
		}
	})
	.onError(ecel).go();
```

### Asset File Helper
The SDK can help you getting asset files, and image assets in the right sizes.

```java
dm.getFileURL("46092f02-7441-4759-b6ff-8f3831d3da4b")
	.locale("en-US")
	.onResponse(new ECResponseListener<URL>{
		@Override
		public void onResponse(URL url){
			// TODO something
		}
	}).go();
```

For image Assets, the following helper is available:

```java
dm.getImageURL("46092f02-7441-4759-b6ff-8f3831d3da4b")
	.size(500)
	.onResponse(new ECResponseListener<URL>{
		@Override
		public void onResponse(URL url){
			// TODO something
		}
	}).go();

// OR

dm.getFileURL("46092f02-7441-4759-b6ff-8f3831d3da4b")
	.image()
	.size(500)
	.onResponse(new ECResponseListener<URL>{
		@Override
		public void onResponse(URL url){
			// TODO something
		}
	}).go();
```
`size(…)` expects a pixel value. The largest edge of the returned image will be at least this value pixels in size, if available.

You can also request a thumbnail:

```java
dm.getImageThumbURL("46092f02-7441-4759-b6ff-8f3831d3da4b")
	.size(100)
	.onResponse(new ECResponseListener<URL>{
		@Override
		public void onResponse(URL url){
			// TODO something
		}
	}).go();

// OR

dm.getImageURL("46092f02-7441-4759-b6ff-8f3831d3da4b")
	.size(500)
	.crop()
	.onResponse(new ECResponseListener<URL>{
		@Override
		public void onResponse(URL url){
			// TODO something
		}
	}).go();
```

### Get Assets

```java
dm.assets()
	.onResponse(new ECResponseListener<List<ECAssets>>{
		@Override
		public void onResponse(List<ECAssets> assets){
			// TODO something
		}
	}).go();
```
### Get Asset

```java
dm.asset("46092f02-7441-4759-b6ff-8f3831d3da4b")… // is shorthand for
dm.assets().filter(new HashMap<String, String>(){{
		put("assetID", "46092f02-7441-4759-b6ff-8f3831d3da4b");
	}})
	.onResponse(new ECResponseListener<ECAsset>{
		@Override
		public void onResponse(ECAsset asset){
			// TODO something
		}
	}).go();
```

### Delete Asset

```java
ECAsset asset;
…
asset.delete()
	.onResponse(new ECResponseListener{
		@Override
		public void onResponse(){
			// TODO something
		}
	}
	.onError(new ECErrorListener{
		@Override
		public void onError(ECError error){
			// TODO something
		}
	}).go();
```

# Documentation

see JavaDoc.

# Test & Coverage

Running tests with:

```
./gradlew test
```

Running tests with coverage:

```
./gradlew jacocoTestReport
```

# Changelog

### 0.2.0
- initial public release