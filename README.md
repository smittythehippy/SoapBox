# Project 2 - *SoapBox*

**SoapBox** is an android app that allows a user to view his Twitter timeline. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **24** hours spent in total

## User Stories
The following **required** functionality is completed:

- [x] User can **compose and post a new tweet**
  - [x] User can click a “Compose” icon in the Action Bar on the top right
  - [x] User can then enter a new tweet and post this to twitter
  - [x] User is taken back to home timeline with **new tweet visible** in timeline
  - [x] Newly created tweet should be manually inserted into the timeline and not rely on a full refresh
  - [x] User can **see a counter with total number of characters left for tweet** on compose tweet page

The following **optional** features are implemented:

- [x] User can **pull down to refresh tweets timeline**
- [x] User is using **"Twitter branded" colors and styles**
- [ ] User sees an **indeterminate progress indicator** when any background or network task is happening
- [ ] User can **select "reply" from detail view to respond to a tweet**
  - [ ] User that wrote the original tweet is **automatically "@" replied in compose**
- [x] User can tap a tweet to **open a detailed tweet view**
  - [ ] User can **take favorite (and unfavorite) or reweet** actions on a tweet
- [x] User can **see embedded image media within a tweet** on list or detail view.

The following **bonus** features are implemented:

- [x] User can view more tweets as they scroll with infinite pagination
- [ ] Compose tweet functionality is build using modal overlay
- [x] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.org/android/Using-Parceler).
- [x] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.org/android/Drawables#vector-drawables) where appropriate.
- [x] User can **click a link within a tweet body** on tweet details view. The click will launch the web browser with relevant page opened.
- [ ] User can view following / followers list through any profile they view.
- [x] User can see embedded image media within the tweet detail view
- [ ] Use the popular ButterKnife annotation library to reduce view boilerplate.
- [x] On the Twitter timeline, leverage the [CoordinatorLayout](http://guides.codepath.org/android/Handling-Scrolls-with-CoordinatorLayout#responding-to-scroll-events) to apply scrolling behavior that [hides / shows the toolbar](http://guides.codepath.org/android/Using-the-App-ToolBar#reacting-to-scroll).
- [ ] User can **open the twitter app offline and see last loaded tweets**. Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from Twitter API, it is also saved for use in offline mode.

The following **additional** features are implemented:

- [x] Appropriate timestamps for detail or list view
- [x] Retweets are correctly displayed, with original user's tweet data

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/smittythehippy/SoapBox/blob/master/walkthrough.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android
- [TimeFormatter](https://github.com/nesquena/TimeFormatter/blob/master/TimeFormatter.java) - Easy relative time formatting
- [Linkifiy](https://github.com/SoapBox/linkifyjs) - Parses bodies of text and creates hyperlinks where appropriate.
- [Parceler](https://github.com/johncarl81/parceler) - Creates a parcelable object that can be used for easy data transfer.

## License

    Copyright [2019] [Kyle Smith]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
