// MelodyMind Dashboard JavaScript
class MelodyMindApp {
    constructor() {
        this.currentUserId = null; // Will be set after session creation
        this.sessionId = null;
        this.currentTab = 'recommendations';
        this.songs = [];
        this.favorites = new Set();
        
        this.init();
    }

    async init() {
        await this.initializeUserSession();
        this.bindEvents();
        this.loadRecommendations();
        this.loadFavoriteCount();
        this.setupTabs();
    }

    async initializeUserSession() {
        // Check if we have a stored user session
        const storedUserId = localStorage.getItem('melodymind_user_id');
        const storedSessionId = localStorage.getItem('melodymind_session_id');
        
        if (storedUserId && storedSessionId) {
            // Validate existing session
            const isValid = await this.validateUserSession(parseInt(storedUserId));
            if (isValid) {
                this.currentUserId = parseInt(storedUserId);
                this.sessionId = storedSessionId;
                console.log('Using existing user session:', this.currentUserId);
                this.updateUserIdDisplay();
                return;
            } else {
                // Clear invalid session
                localStorage.removeItem('melodymind_user_id');
                localStorage.removeItem('melodymind_session_id');
            }
        }
        
        // Create new session
        await this.createUserSession();
    }

    async createUserSession() {
        try {
            const response = await fetch('/api/session/create', {
                method: 'POST'
            });
            const data = await response.json();
            
            if (data.success) {
                this.currentUserId = data.data.user_id;
                this.sessionId = data.data.session_id;
                
                // Store in localStorage
                localStorage.setItem('melodymind_user_id', this.currentUserId.toString());
                localStorage.setItem('melodymind_session_id', this.sessionId);
                
                console.log('Created new user session:', this.currentUserId);
                this.updateUserIdDisplay();
            } else {
                console.error('Failed to create user session:', data.message);
                // Fallback to a random ID
                this.currentUserId = Math.floor(Math.random() * 9000000) + 1000000;
                this.updateUserIdDisplay();
            }
        } catch (error) {
            console.error('Error creating user session:', error);
            // Fallback to a random ID
            this.currentUserId = Math.floor(Math.random() * 9000000) + 1000000;
            this.updateUserIdDisplay();
        }
    }

    updateUserIdDisplay() {
        const userIdElement = document.getElementById('userId');
        if (userIdElement && this.currentUserId) {
            userIdElement.textContent = `#${this.currentUserId}`;
        }
    }

    async validateUserSession(userId) {
        try {
            const response = await fetch(`/api/session/validate?userId=${userId}`);
            const data = await response.json();
            return data.success && data.data === true;
        } catch (error) {
            console.error('Error validating user session:', error);
            return false;
        }
    }

    async refreshUserSession() {
        if (!this.currentUserId) return;
        
        try {
            const response = await fetch(`/api/session/refresh?userId=${this.currentUserId}`, {
                method: 'POST'
            });
            const data = await response.json();
            
            if (data.success) {
                console.log('User session refreshed');
            }
        } catch (error) {
            console.error('Error refreshing user session:', error);
        }
    }

    bindEvents() {
        // Search button
        document.getElementById('searchBtn').addEventListener('click', () => {
            this.handleSearch();
        });

        // Tab buttons
        document.getElementById('recommendationsTab').addEventListener('click', () => {
            this.switchTab('recommendations');
        });
        
        document.getElementById('trendingTab').addEventListener('click', () => {
            this.switchTab('trending');
        });
        
        document.getElementById('favoritesTab').addEventListener('click', () => {
            this.switchTab('favorites');
        });

        // Modal close buttons
        document.getElementById('closeLyricsModal').addEventListener('click', () => {
            this.closeModal('lyricsModal');
        });
        
        document.getElementById('closeChordsModal').addEventListener('click', () => {
            this.closeModal('chordsModal');
        });

        // Close modals on outside click
        document.getElementById('lyricsModal').addEventListener('click', (e) => {
            if (e.target.id === 'lyricsModal') {
                this.closeModal('lyricsModal');
            }
        });
        
        document.getElementById('chordsModal').addEventListener('click', (e) => {
            if (e.target.id === 'chordsModal') {
                this.closeModal('chordsModal');
            }
        });

        // Dark mode toggle
        document.getElementById('darkModeToggle').addEventListener('click', () => {
            this.toggleDarkMode();
        });
    }

    setupTabs() {
        const tabButtons = document.querySelectorAll('.tab-button');
        tabButtons.forEach(button => {
            button.classList.remove('active');
            button.classList.add('text-gray-600', 'hover:text-gray-900', 'hover:bg-gray-100');
        });
        
        document.getElementById('recommendationsTab').classList.add('active');
        document.getElementById('recommendationsTab').classList.remove('text-gray-600', 'hover:text-gray-900', 'hover:bg-gray-100');
        document.getElementById('recommendationsTab').classList.add('bg-primary', 'text-white');
    }

    switchTab(tab) {
        this.currentTab = tab;
        
        // Update tab buttons
        const tabButtons = document.querySelectorAll('.tab-button');
        tabButtons.forEach(button => {
            button.classList.remove('active', 'bg-primary', 'text-white');
            button.classList.add('text-gray-600', 'hover:text-gray-900', 'hover:bg-gray-100');
        });
        
        const activeTab = document.getElementById(`${tab}Tab`);
        activeTab.classList.add('active', 'bg-primary', 'text-white');
        activeTab.classList.remove('text-gray-600', 'hover:text-gray-900', 'hover:bg-gray-100');

        // Load content based on tab
        switch(tab) {
            case 'recommendations':
                this.loadRecommendations();
                break;
            case 'trending':
                this.loadTrending();
                break;
            case 'favorites':
                this.loadFavorites();
                break;
        }
    }

    async handleSearch() {
        if (this.currentTab === 'recommendations') {
            await this.loadRecommendations();
        } else if (this.currentTab === 'trending') {
            await this.loadTrending();
        }
    }

    async loadRecommendations() {
        this.showLoading();
        
        try {
            const params = new URLSearchParams();
            
            const genre = document.getElementById('genreFilter').value;
            const language = document.getElementById('languageFilter').value;
            const region = document.getElementById('regionFilter').value;
            const mood = document.getElementById('moodFilter').value;
            
            if (genre) params.append('genre', genre);
            if (language) params.append('language', language);
            if (region) params.append('region', region);
            if (mood) params.append('mood', mood);
            params.append('userId', this.currentUserId);
            
            const response = await fetch(`/api/music/recommendations?${params}`);
            const data = await response.json();
            
            if (data.success) {
                this.songs = data.data;
                this.renderSongs(this.songs);
            } else {
                this.showError('Failed to load recommendations');
            }
        } catch (error) {
            console.error('Error loading recommendations:', error);
            this.showError('Failed to load recommendations');
        } finally {
            this.hideLoading();
        }
    }

    async loadTrending() {
        this.showLoading();
        
        try {
            const region = document.getElementById('regionFilter').value || 'US';
            const response = await fetch(`/api/music/trending?region=${region}&userId=${this.currentUserId}`);
            const data = await response.json();
            
            if (data.success) {
                this.songs = data.data;
                this.renderSongs(this.songs);
            } else {
                this.showError('Failed to load trending songs');
            }
        } catch (error) {
            console.error('Error loading trending songs:', error);
            this.showError('Failed to load trending songs');
        } finally {
            this.hideLoading();
        }
    }

    async loadFavorites() {
        this.showLoading();
        
        try {
            const response = await fetch(`/api/music/favorites?userId=${this.currentUserId}`);
            const data = await response.json();
            
            if (data.success) {
                this.songs = data.data;
                this.renderSongs(this.songs);
            } else {
                this.showError('Failed to load favorites');
            }
        } catch (error) {
            console.error('Error loading favorites:', error);
            this.showError('Failed to load favorites');
        } finally {
            this.hideLoading();
        }
    }

    async loadFavoriteCount() {
        try {
            const response = await fetch(`/api/music/favorites/count?userId=${this.currentUserId}`);
            const data = await response.json();
            
            if (data.success) {
                document.getElementById('favoriteCount').textContent = data.data;
            }
        } catch (error) {
            console.error('Error loading favorite count:', error);
        }
    }

    renderSongs(songs) {
        const container = document.getElementById('songsContainer');
        const emptyState = document.getElementById('emptyState');
        
        if (!songs || songs.length === 0) {
            container.innerHTML = '';
            emptyState.classList.remove('hidden');
            return;
        }
        
        emptyState.classList.add('hidden');
        
        container.innerHTML = songs.map(song => this.createSongCard(song)).join('');
    }

    createSongCard(song) {
        const isFavorite = song.is_favorite || false;
        const favoriteIcon = isFavorite ? 'fas fa-heart text-red-500' : 'far fa-heart text-gray-400';
        const favoriteText = isFavorite ? 'Remove from Favorites' : 'Add to Favorites';
        
        return `
            <div class="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition duration-300">
                <div class="p-6">
                    <div class="flex justify-between items-start mb-4">
                        <div class="flex-1">
                            <h3 class="text-xl font-semibold text-gray-900 mb-1">${song.title}</h3>
                            <p class="text-gray-600 mb-2">${song.artist}</p>
                            <div class="flex flex-wrap gap-2 mb-3">
                                ${song.genre ? `<span class="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full">${song.genre}</span>` : ''}
                                ${song.mood ? `<span class="px-2 py-1 bg-purple-100 text-purple-800 text-xs rounded-full">${song.mood}</span>` : ''}
                                ${song.language ? `<span class="px-2 py-1 bg-green-100 text-green-800 text-xs rounded-full">${song.language}</span>` : ''}
                            </div>
                        </div>
                        <button onclick="app.toggleFavorite(${song.id}, ${!isFavorite})" 
                                class="p-2 hover:bg-gray-100 rounded-full transition duration-200"
                                title="${favoriteText}">
                            <i class="${favoriteIcon}"></i>
                        </button>
                    </div>
                    
                    ${song.preview_url ? `
                        <audio controls class="w-full mb-4">
                            <source src="${song.preview_url}" type="audio/mpeg">
                            Your browser does not support the audio element.
                        </audio>
                    ` : ''}
                    
                    <div class="flex flex-wrap gap-2">
                        <button onclick="app.showLyrics('${song.artist}', '${song.title}')" 
                                class="flex-1 bg-green-500 hover:bg-green-600 text-white px-3 py-2 rounded-lg text-sm font-medium transition duration-200">
                            <i class="fas fa-quote-left mr-1"></i>Lyrics
                        </button>
                        <button onclick="app.showChords('${song.artist}', '${song.title}')" 
                                class="flex-1 bg-purple-500 hover:bg-purple-600 text-white px-3 py-2 rounded-lg text-sm font-medium transition duration-200">
                            <i class="fas fa-music mr-1"></i>Chords
                        </button>
                    </div>
                    
                    <div class="mt-3 flex justify-between items-center text-xs text-gray-500">
                        <span><i class="fas fa-star mr-1"></i>${song.popularity_score || 0}/100</span>
                        <span>${song.provider || 'Unknown'}</span>
                    </div>
                </div>
            </div>
        `;
    }

    async toggleFavorite(songId, addToFavorites) {
        if (!this.currentUserId) {
            this.showError('User session not initialized. Please refresh the page.');
            return;
        }
        
        try {
            const endpoint = addToFavorites ? '/api/music/favorites/add' : '/api/music/favorites/remove';
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    userId: this.currentUserId,
                    songId: songId
                })
            });
            
            const data = await response.json();
            
            if (data.success) {
                // Refresh current view
                if (this.currentTab === 'favorites') {
                    this.loadFavorites();
                } else {
                    // Update the heart icon in the current view
                    this.loadRecommendations();
                }
                this.loadFavoriteCount();
                
                this.showNotification(addToFavorites ? 'Added to favorites!' : 'Removed from favorites!');
                
                // Refresh session to update favorite count
                this.refreshUserSession();
            } else {
                this.showError(data.message || 'Failed to update favorites');
            }
        } catch (error) {
            console.error('Error updating favorites:', error);
            this.showError('Failed to update favorites');
        }
    }

    async showLyrics(artist, title) {
        try {
            const response = await fetch(`/api/music/lyrics?artist=${encodeURIComponent(artist)}&title=${encodeURIComponent(title)}`);
            const data = await response.json();
            
            if (data.success) {
                document.getElementById('lyricsContent').textContent = data.data;
                document.getElementById('lyricsModal').classList.remove('hidden');
            } else {
                this.showError('Failed to load lyrics');
            }
        } catch (error) {
            console.error('Error loading lyrics:', error);
            this.showError('Failed to load lyrics');
        }
    }

    async showChords(artist, title) {
        try {
            const response = await fetch(`/api/music/chords?artist=${encodeURIComponent(artist)}&title=${encodeURIComponent(title)}`);
            const data = await response.json();
            
            if (data.success) {
                document.getElementById('chordsContent').textContent = data.data;
                document.getElementById('chordsModal').classList.remove('hidden');
            } else {
                this.showError('Failed to load chords');
            }
        } catch (error) {
            console.error('Error loading chords:', error);
            this.showError('Failed to load chords');
        }
    }

    closeModal(modalId) {
        document.getElementById(modalId).classList.add('hidden');
    }

    showLoading() {
        document.getElementById('loadingSpinner').classList.remove('hidden');
        document.getElementById('songsContainer').classList.add('hidden');
        document.getElementById('emptyState').classList.add('hidden');
    }

    hideLoading() {
        document.getElementById('loadingSpinner').classList.add('hidden');
        document.getElementById('songsContainer').classList.remove('hidden');
    }

    showError(message) {
        this.showNotification(message, 'error');
    }

    showNotification(message, type = 'success') {
        // Create notification element
        const notification = document.createElement('div');
        notification.className = `fixed top-4 right-4 z-50 px-6 py-3 rounded-lg shadow-lg transition-all duration-300 ${
            type === 'error' ? 'bg-red-500 text-white' : 'bg-green-500 text-white'
        }`;
        notification.textContent = message;
        
        document.body.appendChild(notification);
        
        // Remove after 3 seconds
        setTimeout(() => {
            notification.remove();
        }, 3000);
    }

    toggleDarkMode() {
        // Simple dark mode toggle (you can expand this)
        document.body.classList.toggle('dark');
        const icon = document.querySelector('#darkModeToggle i');
        icon.classList.toggle('fa-moon');
        icon.classList.toggle('fa-sun');
    }
}

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.app = new MelodyMindApp();
});