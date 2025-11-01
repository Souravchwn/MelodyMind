// MelodyMind Admin Dashboard JavaScript
class AdminDashboard {
    constructor() {
        this.init();
    }

    init() {
        this.loadSchedulerStats();
        this.loadProviderStatus();
        
        // Refresh data every 30 seconds
        setInterval(() => {
            this.loadSchedulerStats();
            this.loadProviderStatus();
        }, 30000);
    }

    async loadSchedulerStats() {
        try {
            const response = await fetch('/api/scheduler/stats');
            const data = await response.json();
            
            if (data.success) {
                this.updateSchedulerStats(data.data);
            } else {
                this.showError('Failed to load scheduler stats');
            }
        } catch (error) {
            console.error('Error loading scheduler stats:', error);
            this.showError('Failed to load scheduler stats');
        }
    }

    async loadProviderStatus() {
        try {
            const response = await fetch('/api/providers/list');
            const data = await response.json();
            
            if (data.success) {
                this.updateProviderList(data.data);
            } else {
                this.showError('Failed to load provider status');
            }
        } catch (error) {
            console.error('Error loading provider status:', error);
            this.showError('Failed to load provider status');
        }
    }

    updateSchedulerStats(stats) {
        // Update status cards
        document.getElementById('lastCleanup').textContent = 
            stats.lastCleanupTime ? this.formatDateTime(stats.lastCleanupTime) : 'Never';
        
        document.getElementById('lastTrending').textContent = 
            stats.lastTrendingRefresh ? this.formatDateTime(stats.lastTrendingRefresh) : 'Never';
        
        document.getElementById('songsCleanedUp').textContent = 
            stats.totalSongsCleanedUp.toLocaleString();
        
        document.getElementById('enabledProviders').textContent = stats.enabledProviders;

        // Update statistics
        document.getElementById('totalCleanups').textContent = stats.totalCleanups;
        document.getElementById('totalTrendingRefreshes').textContent = stats.totalTrendingRefreshes;
        
        const statusElement = document.getElementById('systemStatus');
        statusElement.textContent = stats.status || 'UNKNOWN';
        
        // Update status color
        statusElement.className = 'text-3xl font-bold ' + this.getStatusColor(stats.status);

        // Update cache info
        this.updateCacheInfo(stats.cacheNames);
    }

    updateProviderList(providers) {
        const container = document.getElementById('providerList');
        
        container.innerHTML = providers.map(provider => `
            <div class="flex items-center justify-between p-3 border border-gray-200 rounded-lg">
                <div class="flex items-center">
                    <div class="w-3 h-3 rounded-full mr-3 ${provider.enabled ? 'bg-green-500' : 'bg-red-500'}"></div>
                    <span class="font-medium text-gray-900">${provider.name}</span>
                </div>
                <div class="flex items-center space-x-3">
                    <span class="text-sm text-gray-500">Priority: ${provider.priority}</span>
                    <span class="px-2 py-1 text-xs rounded-full ${
                        provider.enabled ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    }">
                        ${provider.enabled ? 'Enabled' : 'Disabled'}
                    </span>
                </div>
            </div>
        `).join('');
    }

    updateCacheInfo(cacheNames) {
        const container = document.getElementById('cacheInfo');
        
        const cacheConfig = {
            'recommendations': { color: 'blue', expiry: '1 hour', description: 'User recommendations' },
            'trending': { color: 'green', expiry: '24 hours', description: 'Trending songs' },
            'lyrics': { color: 'purple', expiry: '7 days', description: 'Song lyrics' },
            'chords': { color: 'orange', expiry: '7 days', description: 'Piano chords' }
        };

        container.innerHTML = Array.from(cacheNames).map(cacheName => {
            const config = cacheConfig[cacheName] || { color: 'gray', expiry: 'Unknown', description: 'Other cache' };
            return `
                <div class="p-4 border border-gray-200 rounded-lg">
                    <div class="flex items-center mb-2">
                        <div class="w-3 h-3 rounded-full bg-${config.color}-500 mr-2"></div>
                        <span class="font-medium text-gray-900 capitalize">${cacheName}</span>
                    </div>
                    <p class="text-sm text-gray-600 mb-1">${config.description}</p>
                    <p class="text-xs text-gray-500">Expires: ${config.expiry}</p>
                </div>
            `;
        }).join('');
    }

    getStatusColor(status) {
        switch (status) {
            case 'HEALTHY': return 'text-green-600';
            case 'OVERDUE': return 'text-red-600';
            case 'NEVER_RUN': return 'text-yellow-600';
            default: return 'text-gray-600';
        }
    }

    formatDateTime(dateTimeString) {
        const date = new Date(dateTimeString);
        return date.toLocaleString();
    }

    showLoading() {
        document.getElementById('loadingModal').classList.remove('hidden');
    }

    hideLoading() {
        document.getElementById('loadingModal').classList.add('hidden');
    }

    showNotification(message, type = 'success') {
        const notification = document.createElement('div');
        notification.className = `fixed top-4 right-4 z-50 px-6 py-3 rounded-lg shadow-lg transition-all duration-300 ${
            type === 'error' ? 'bg-red-500 text-white' : 'bg-green-500 text-white'
        }`;
        notification.textContent = message;
        
        document.body.appendChild(notification);
        
        setTimeout(() => {
            notification.remove();
        }, 5000);
    }

    showError(message) {
        this.showNotification(message, 'error');
    }
}

// Action functions
async function triggerManualCleanup() {
    if (!confirm('Are you sure you want to trigger a manual cleanup? This will clear caches and clean old data.')) {
        return;
    }

    adminDashboard.showLoading();
    
    try {
        const response = await fetch('/api/scheduler/cleanup/manual', {
            method: 'POST'
        });
        const data = await response.json();
        
        if (data.success) {
            adminDashboard.showNotification('Manual cleanup completed successfully');
            adminDashboard.loadSchedulerStats();
        } else {
            adminDashboard.showError('Manual cleanup failed: ' + data.message);
        }
    } catch (error) {
        console.error('Error triggering manual cleanup:', error);
        adminDashboard.showError('Failed to trigger manual cleanup');
    } finally {
        adminDashboard.hideLoading();
    }
}

async function refreshTrending() {
    adminDashboard.showLoading();
    
    try {
        const response = await fetch('/api/scheduler/trending/refresh', {
            method: 'POST'
        });
        const data = await response.json();
        
        if (data.success) {
            adminDashboard.showNotification('Trending songs refreshed successfully');
            adminDashboard.loadSchedulerStats();
        } else {
            adminDashboard.showError('Trending refresh failed: ' + data.message);
        }
    } catch (error) {
        console.error('Error refreshing trending:', error);
        adminDashboard.showError('Failed to refresh trending songs');
    } finally {
        adminDashboard.hideLoading();
    }
}

async function clearCache() {
    if (!confirm('Are you sure you want to clear all caches? This will temporarily slow down responses.')) {
        return;
    }

    adminDashboard.showLoading();
    
    try {
        const response = await fetch('/api/scheduler/cache/clear', {
            method: 'POST'
        });
        const data = await response.json();
        
        if (data.success) {
            adminDashboard.showNotification('Cache cleared successfully');
        } else {
            adminDashboard.showError('Cache clear failed: ' + data.message);
        }
    } catch (error) {
        console.error('Error clearing cache:', error);
        adminDashboard.showError('Failed to clear cache');
    } finally {
        adminDashboard.hideLoading();
    }
}

async function preloadRecommendations() {
    adminDashboard.showLoading();
    
    try {
        const response = await fetch('/api/scheduler/preload', {
            method: 'POST'
        });
        const data = await response.json();
        
        if (data.success) {
            adminDashboard.showNotification('Recommendations preloaded successfully');
        } else {
            adminDashboard.showError('Preload failed: ' + data.message);
        }
    } catch (error) {
        console.error('Error preloading recommendations:', error);
        adminDashboard.showError('Failed to preload recommendations');
    } finally {
        adminDashboard.hideLoading();
    }
}

// Initialize the admin dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.adminDashboard = new AdminDashboard();
});