# Popular TV Series App

# Overview
    This Android application showcases popular TV series using The Movie Database (TMDb) API. It demonstrates 
    Android development concepts such as UI design with Jetpack Compose, data handling, networking, and offline support.

# Features
    * Splash Screen: Displays a centered logo.
    * Home Screen: Series banners at the top and a list of popular series with pagination and offline support.
    * Search Screen: Filter series based on search query with pagination support.
    * Series Screen: Detailed information about each TV series.
    * Season Screen: List of episodes for the selected season of a series.
    * Pull to Refresh: Refresh the list and fetch the latest series data, every page has pull to refresh.
    * Loading and Error States: UI for loading and toast message for error states.
    * No Internet Connection Screen: Shown for screens other than the Home Screen when there is no internet connection.
    * Offline Support: Available for the Home Screen.


# Technical Implementation
# Architecture
    * ViewModel: Manages UI-related data.
    * LiveData: Keeps UI elements updated.
    * Repository Pattern: Manages data operations and abstracts the data source.
    * Room Database: Stores fetched data locally for offline support.

# Libraries Used
    * Kotlin: Programming language.
    * Jetpack Compose: UI building with Material3 components.
    * Retrofit: Networking and API calls.
    * Room: Local database and offline support.
    * Coil: Image loading.
    * Paging: Handling data pagination.
    * Hilt: Dependency injection.
    * Coroutines: Asynchronous programming.

# Key Components
    * MainActivity: Hosts main navigation and TV series list.
    * SeriesViewModel: Manages state and data for TV series list.
    * SeriesRepository: Handles data operations and interactions.
    * SeriesDao: Defines methods for accessing Room database.
    * ApiService: Defines TMDb API endpoints.
    * NetworkModule: Provides network-related dependencies.


# UI Components
    * SplashScreen: Displays centered logo.
    * HomeScreen: Popular TV series list with pagination and banners.
    * SearchScreen: Search TV series with pagination support.
    * DetailsScreen: Detailed information about a TV series.
    * SeasonScreen: Episodes list for the selected season.
    * LoadingState: Loading indicators.
    * ErrorState: Error messages.
    * NoInternetScreen: No internet connection message for certain screens.


# Offline Support
    * Room database caches data locally, ensuring the app functions without an internet connection.
      The data fetched from the TMDb API is stored in the local database for the Home Screen.


# Decisions Made
    * Jetpack Compose: Modern UI building.
    * Retrofit and Room: Efficient network requests and local data storage.
    * Paging: Efficient handling of large datasets.
    * Hilt: Effective dependency management.
    * Coil: Image loading compatible with Jetpack Compose.
    * API Integration
    * API Helper
    * Defines API call methods, implemented by ApiHelperImpl using Retrofit.



# API Service
    Retrofit interface defining TMDb API endpoints.

# Repository Pattern
    Manages data operations, interacting with remote data via ApiHelper and local data via DAO.

# ViewModel
    Manages UI-related data, using LiveData and Pager for pagination.

# Dependency Injection with Hilt
    Provides dependencies for network operations, including Retrofit and ApiService.

# Constants
    Contains constant values like the TMDb API base URL.

# PopularSeriesApplication
    Sets up Hilt for dependency injection, managing dependencies throughout the app's lifecycle.



# Note
    **** The TMDB website and its APIs do not work with Jio SIM network but are available on Airtel, 
         other SIM networks, and WiFi networks.  So check this app with prefered Networks ****