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

TODO add example reference.

### Initialization
You need to connect to your Data Manager API using the `DataManager(…)` constructors.

Initializing dataManager with existing token:

```java
DataManager dm = new DataManager(
  new URL("https://datamanager.entrecode.de/api/beefbeef"),
  UUID.fromString("8c3b7b55-531f-4a03-b584-09fdef59cb0c")
);
```

Initializing without token (will be generated):

```java
DataManager dm = new DataManager(
  new URL("https://datamanager.entrecode.de/api/beefbeef")
);
```

Alternative without token:

```java
DataManager dm = new DataManager("beefbeef"); // throws MalformedURLException
```

Initializing with read-only mode:

```java
DataManager dm = new DataManager("beefbeef", true); // throws MalformedURLException
```

### Get Entries 
TODO filter

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
	}).go();
```

### Get Entry
TODO short hand hint. is `dm.model().entries(…, filter);`

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
dm.model("myModel).createEntry(ecEntry)
	.onError(new ECResponseListener<ECEntry>{
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
dm.modelList(
	new ECResponseListener<List<ECModel>>{
		@Override
		public void onResponse(List<ECModel> models){
			// TODO something
		}
	}
);
```

### Get JSON Schema

```java
dm.model("myModel").getSchema()
	.for("PUT")
	.onResponse(new ECResponseListener<ECJsonSchema>{ // TODO
		@Override
		public void onResponse(ECJsonSchema schema){
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
dm.register()
	.onResponse(new ECResponseListener<UUID>{ // TODO
		@Override
		public void onResponse(UUID token){
			// TODO save token
		}
	})
	.onError(new ECErrorListener{
		@Override
		public void onError(){
			// TODO something
		}
	}).go();
```

`accessToken` is a property of the DataManager instance gettable via `getToken()`.

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
			// TODO ?!? how to edit
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
	.noCrop()
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
dm.asset("46092f02-7441-4759-b6ff-8f3831d3da4b")
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

### DataManager Class
#### Constructors
##### `public DataManager(URL url)`
for initializing a `DataManager` with creating of a new user token.

##### `public DataManager(URL url, UUID accessToken)`
for initializing a `DataManager` with existing user token.

##### `public DataManager(URL url, boolean readOnly)`
for initializing a `DataManager` in read-only mode.

##### `public DataManager(String id) throws MalformedURLException`
for initalizing a `DataManager` by its `id` and creating of a new user token.

`id` must be a `String` matching the regex `/^[0-9a-fA-F]{8}$/`.

##### `public DataManager(String id, UUID accessToken) throws MalformedURLException`
for initializing a `Datamanager` by its `id` and existing user token.

`id` must be a `String` matching the regex `/^[0-9a-fA-F]{8}$/`.

##### `public DataManager(String id, boolean readOnly) throws MalformedURLException`
for initializing a `DataManager` by its `id` in read-only mode.

`id` must be a `String` matching the regex `/^[0-9a-fA-F]{8}$/`.

#### Getter & Setter
##### `public UUID getToken()`
retrieves the current `token` used by this `DataManager`. Can be used for saving a generated `token`.

# Test & Coverage

Running tests with:

```
./gradlew test
```

Running tests with coverage:

```
./gradlew coverage
```

# Changelog

### 0.2.0
- initial public release