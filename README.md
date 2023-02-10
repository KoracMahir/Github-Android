<div align="center">
  <img src="https://user-images.githubusercontent.com/29480276/217962313-7ad28d8b-ed35-4663-b7dd-5f3a79e653bb.svg" width="200px">
  <h1>Github Android application</h1>
</div>

<p align="center">
  Project was made as an Android application showing Github repository information. User can search for github repositories and check short description by click on items inside the list. Application has 2 variants free and paid where free users cannot open more then 10 repository details and can't log in to check their profile data. 
</p>

![Github app](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white) ![MVVM](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)

## Features

* Search screen - able to enter repository name
* Details screen - show more detail information about repository and navigate to browser to show more data for given repository
* Current user profile screen - use OAuth to sign in and display current signed in user information

## Technologies used

Technology | Purpose
--- | ---
retrofit | Fetch API requests
coroutines | handle thread getting remote data
mvvm | organize project enables me to extend project in future
glide | Display images
navigation component | Navigate trough screens
hilt | inject view model components

## Demo

Search | Detils screen | Profile screen
--- | --- | ---
![search demo](https://user-images.githubusercontent.com/29480276/217966902-68eaed1f-7021-47ed-865f-005c011a9518.gif) | ![details demo](https://user-images.githubusercontent.com/29480276/217967745-fb43e7c0-c7a4-465b-8cc4-f3a40efa9e29.gif) | ![profile screen](https://user-images.githubusercontent.com/29480276/217968089-7e739955-e813-497e-b5c9-ff5367da8a2a.gif)



## TODO

* [ ] Add data binding
* [ ] Add more functionalities.
