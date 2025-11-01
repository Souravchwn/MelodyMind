# 🎵 MelodyMind – Smart Music Recommendation Dashboard (SRS)

## 1. Introduction

### 1.1 Purpose

MelodyMind 2.0 is an intelligent music recommendation system that fetches songs from multiple APIs, provides AI-driven suggestions, lyrics, and piano chords. It allows users to explore music by **genre, language, region, and mood**, and save their **favorite tracks**.

The system emphasizes **modern Java 25 features**, including **virtual threads** for high concurrency, and a lightweight **Tailwind-based static UI**.

### 1.2 Scope

* Aggregate music recommendations from multiple providers (Spotify, LastFM, Deezer).
* Display song previews with audio playback.
* Provide lyrics and piano chord visualizations.
* Maintain user favorites in-memory (H2 database) or optionally persist in production.
* Fast, responsive dashboard using TailwindCSS.
* Modular plugin-based architecture for future API integrations.

### 1.3 Definitions

* **Virtual Thread:** Lightweight thread in Java 25.
* **Music Provider Plugin:** Module to fetch songs from a specific API.
* **Favorites:** User-saved tracks for quick access.
* **Chords:** Piano chords associated with a song.
* **Lyrics:** Textual content of a song.

---

## 2. Overall Description

### 2.1 Product Perspective

MelodyMind 2.0 is a standalone web application, backend in **Spring Boot**, frontend in **static HTML + Tailwind**.

It is easily extendable to new APIs, AI services for mood detection, and audio analysis plugins.

### 2.2 Product Features

* Multi-provider music recommendation aggregation.
* Filtering by **genre, language, region, mood**.
* Audio previews directly in the dashboard.
* Lyrics and piano chords enrichment.
* User favorites management.
* High-concurrency using **Java 25 virtual threads**.
* Tailwind responsive UI with dark mode.

### 2.3 User Classes

* **General User:** Browse music, listen to previews, save favorites.
* **Developer:** Add new providers or enrichment modules.
* **Music Analyst:** Review trends, logs, and favorite counts.

### 2.4 Operating Environment

* JVM Java 25
* Spring Boot 3.4+
* Browser (desktop/mobile)
* H2 in-memory database

---

## 3. System Features

### 3.1 Multi-Provider Aggregation

**Description:** Concurrently fetch songs from registered providers.
**Functional Requirements:**

* Endpoint `/api/music/recommendations` accepts `genre`, `language`, `region`.
* Deduplicate tracks by title + artist.
* Virtual threads for each provider fetch.

### 3.2 Audio Preview

* Each song includes a 30-second preview URL.
* `<audio>` player embedded in dashboard.

### 3.3 Lyrics & Piano Chords

* Lyrics fetched from open-source or free APIs.
* Piano chords displayed alongside the song.
* Optional modal to show full chords.

### 3.4 User Favorites

* Add/remove favorite tracks.
* Favorites stored per user (in-memory DB).
* Endpoint `/api/music/favorites` manages favorites.

### 3.5 Tailwind Dashboard

* Filters: genre, language, region, mood
* Responsive cards showing song info, audio preview, lyrics/chords buttons
* Modal pop-ups for lyrics and chords

---

## 4. External Interfaces

### 4.1 Software Interfaces

* Spotify / LastFM / Deezer APIs
* Lyrics APIs (lyrics.ovh, Genius)
* Optional AI services (HuggingFace, OpenAI)

### 4.2 User Interfaces

* Static HTML + Tailwind CSS UI
* JS fetch calls to backend endpoints

### 4.3 Communications Interfaces

* REST JSON API
* Query parameters for filters
* Cross-origin allowed for frontend

---

## 5. Data Model

### Song Entity

| Field       | Type      | Description             |
| ----------- | --------- | ----------------------- |
| id          | BIGINT    | Primary key             |
| title       | VARCHAR   | Song title              |
| artist      | VARCHAR   | Artist name             |
| genre       | VARCHAR   | Song genre              |
| language    | VARCHAR   | Language code           |
| region      | VARCHAR   | Region code             |
| previewUrl  | VARCHAR   | 30-second audio preview |
| lyrics      | TEXT      | Song lyrics             |
| pianoChords | TEXT      | Piano chords            |
| fetchedAt   | TIMESTAMP | Time song was fetched   |

### UserFavorite Entity

| Field   | Type      | Description                 |
| ------- | --------- | --------------------------- |
| userId  | BIGINT    | User identifier             |
| songId  | BIGINT    | Song identifier             |
| addedAt | TIMESTAMP | When the favorite was added |

---

## 6. API Endpoints

| Endpoint                           | Method | Description                  |
| ---------------------------------- | ------ | ---------------------------- |
| `/api/music/recommendations`       | GET    | Fetch recommended songs      |
| `/api/music/favorites`             | GET    | List user favorites          |
| `/api/music/favorites/add`         | POST   | Add a song to favorites      |
| `/api/music/favorites/remove`      | POST   | Remove a song from favorites |
| `/api/music/lyrics?artist=&title=` | GET    | Get song lyrics              |
| `/api/music/chords?artist=&title=` | GET    | Get piano chords             |
| `/api/music/trending`              | GET    | Fetch trending songs         |

---

## 7. Non-Functional Requirements

* Performance: Responses <200ms (cached), first fetch <2s per provider
* Scalability: Handle 1000+ concurrent users with virtual threads
* Reliability: Fallback to cached results if provider fails
* Security: API keys hidden, optional authentication for favorites
* Maintainability: Modular plugin provider architecture
* Extensibility: Add new APIs or enrichment modules easily
* Usability: Responsive UI, clear filters, modal previews

---

## 8. System Architecture

```
Frontend (HTML + Tailwind)
        │
        ▼
Controller (Spring Boot REST Endpoints)
        │
        ▼
Service Layer (Aggregator + Lyrics + Chords + Favorites)
        │
        ▼
Provider Plugins (Spotify, LastFM, Deezer)
        │
        ▼
In-Memory Database (H2)
```

* Virtual threads handle **API fetches and enrichment concurrently**.
* Preloading and caching improve response times.

---

## 9. Future Enhancements

* AI Mood Detection for personalized recommendations
* Download chords/lyrics PDF
* Full song playlist integration
* Progressive Web App (offline favorites)
* Real-time trending analytics

---

## 10. Glossary

* **Provider Plugin:** Module that fetches songs from a specific API.
* **Virtual Thread:** Lightweight thread for concurrent tasks (Java 25).
* **Favorites:** Tracks saved by a user.
* **Chords:** Piano chords for a song.
* **Enrichment:** Lyrics or chord information attached to a song.

---

✅ **MelodyMind is now fully ready for developers to fork, extend, and deploy.**

---