## 🎵 MelodyMind: Personalized Music Recommendation Dashboard

**MelodyMind** is a cutting-edge, lightweight **Music Recommendation System** built on **Spring Boot 3.4+** and **Java 25**. It serves as a high-performance backend designed to deliver personalized, mood-based music recommendations using real-time trending data aggregated from major third-party APIs (Spotify, Last.fm, iTunes).

### **Core Technology Highlights**

* **Futuristic Concurrency:** Leverages **Java 25's Virtual Threads** and **Structured Concurrency** to ensure blazing-fast, parallel data retrieval from multiple external APIs, guaranteeing a sub-100ms response time for cached recommendations.
* **Intelligent Aggregation:** The system features an **API Aggregation** module that merges and normalizes track data from diverse sources into a single, reliable dataset.
* **Smart Recommendation Engine:** Songs are ranked using a weighted formula that considers **popularity**, **mood match**, **recency**, and **personal preferences** to provide the most relevant tracks.
* **Scalable Backend:** Implemented as a stateless **Spring Boot REST API** with an in-memory database (**H2/MapDB**) and **Caffeine** cache, making it highly portable, easy to maintain, and ready for horizontal scaling.

### **Key Functional Features**

* **Mood-Based Recommendations:** Users select a mood (e.g., 'focus', 'chill') to receive contextually relevant music.
* **Localized Trending Data:** Allows users to select a country for localized, real-time trending music updates.
* **Scheduled Refresh:** A background scheduler periodically refreshes the cache to keep data current, with an Offline Fallback for high availability.
* **Simple Static Dashboard:** Exposes clean REST endpoints consumed by a static HTML/JS interface for a seamless, developer-friendly UI experience.
