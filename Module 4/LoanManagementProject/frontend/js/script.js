// Northern Arc Loan Management System - Main JavaScript

// Utility Functions

/**
 * Format number as Indian currency
 */
function formatCurrency(amount) {
    return '₹' + parseFloat(amount).toLocaleString('en-IN', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    });
}

/**
 * Format date as DD/MM/YYYY
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

/**
 * Get user role from session
 */
function getCurrentUser() {
    const userJson = sessionStorage.getItem('currentUser');
    return userJson ? JSON.parse(userJson) : null;
}

/**
 * Check if user is authenticated
 */
function isAuthenticated() {
    return sessionStorage.getItem('currentUser') !== null;
}

/**
 * Check if user has admin/manager role
 */
function isAdminOrManager() {
    const user = getCurrentUser();
    return user && (user.role === 'ADMIN' || user.role === 'MANAGER');
}

/**
 * Logout user
 */
function logout() {
    sessionStorage.removeItem('currentUser');
    window.location.href = 'index.html';
}

/**
 * Show toast notification
 */
function showToast(message, type = 'info') {
    const toastContainer = document.getElementById('toastContainer');
    if (!toastContainer) {
        createToastContainer();
    }
    
    const toastId = 'toast-' + Date.now();
    const toastHTML = `
        <div id="${toastId}" class="toast align-items-center text-white bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;
    
    document.getElementById('toastContainer').insertAdjacentHTML('beforeend', toastHTML);
    
    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement, { delay: 3000 });
    toast.show();
    
    // Remove toast from DOM after it's hidden
    toastElement.addEventListener('hidden.bs.toast', function() {
        toastElement.remove();
    });
}

/**
 * Create toast container if it doesn't exist
 */
function createToastContainer() {
    const container = document.createElement('div');
    container.id = 'toastContainer';
    container.className = 'toast-container position-fixed top-0 end-0 p-3';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
}

/**
 * Validate email format
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Validate phone number (Indian format)
 */
function isValidPhone(phone) {
    const phoneRegex = /^[6-9]\d{9}$/;
    return phoneRegex.test(phone.replace(/\s+/g, ''));
}

/**
 * Calculate EMI
 */
function calculateEMI(principal, annualRate, tenureMonths) {
    const monthlyRate = annualRate / 12 / 100;
    const emi = principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths) / 
                (Math.pow(1 + monthlyRate, tenureMonths) - 1);
    return Math.round(emi);
}

/**
 * Debounce function for search inputs
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Search/Filter table rows
 */
function filterTableRows(tableId, searchValue, additionalFilters = {}) {
    const table = document.getElementById(tableId);
    if (!table) return;
    
    const tbody = table.querySelector('tbody');
    const rows = tbody.getElementsByTagName('tr');
    
    for (let row of rows) {
        const text = row.textContent || row.innerText;
        let showRow = true;
        
        // Apply search filter
        if (searchValue && text.toUpperCase().indexOf(searchValue.toUpperCase()) === -1) {
            showRow = false;
        }
        
        // Apply additional filters
        for (let [key, value] of Object.entries(additionalFilters)) {
            if (value && text.toUpperCase().indexOf(value.toUpperCase()) === -1) {
                showRow = false;
                break;
            }
        }
        
        row.style.display = showRow ? '' : 'none';
    }
}

/**
 * Sort table by column
 */
function sortTable(tableId, columnIndex, ascending = true) {
    const table = document.getElementById(tableId);
    if (!table) return;
    
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr'));
    
    rows.sort((a, b) => {
        const aValue = a.cells[columnIndex].textContent.trim();
        const bValue = b.cells[columnIndex].textContent.trim();
        
        // Try to parse as numbers
        const aNum = parseFloat(aValue.replace(/[^0-9.-]+/g, ''));
        const bNum = parseFloat(bValue.replace(/[^0-9.-]+/g, ''));
        
        if (!isNaN(aNum) && !isNaN(bNum)) {
            return ascending ? aNum - bNum : bNum - aNum;
        }
        
        // String comparison
        return ascending ? 
            aValue.localeCompare(bValue) : 
            bValue.localeCompare(aValue);
    });
    
    // Re-append sorted rows
    rows.forEach(row => tbody.appendChild(row));
}

/**
 * Export table to CSV
 */
function exportTableToCSV(tableId, filename = 'export.csv') {
    const table = document.getElementById(tableId);
    if (!table) return;
    
    let csv = [];
    const rows = table.querySelectorAll('tr');
    
    for (let row of rows) {
        let rowData = [];
        const cells = row.querySelectorAll('td, th');
        
        for (let cell of cells) {
            rowData.push('"' + cell.textContent.trim().replace(/"/g, '""') + '"');
        }
        
        csv.push(rowData.join(','));
    }
    
    // Download CSV
    const csvContent = csv.join('\n');
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);
    
    link.setAttribute('href', url);
    link.setAttribute('download', filename);
    link.style.visibility = 'hidden';
    
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

/**
 * Print page content
 */
function printPage() {
    window.print();
}

/**
 * Copy text to clipboard
 */
function copyToClipboard(text) {
    if (navigator.clipboard) {
        navigator.clipboard.writeText(text).then(() => {
            showToast('Copied to clipboard!', 'success');
        }).catch(() => {
            showToast('Failed to copy', 'danger');
        });
    } else {
        // Fallback for older browsers
        const textArea = document.createElement('textarea');
        textArea.value = text;
        document.body.appendChild(textArea);
        textArea.select();
        try {
            document.execCommand('copy');
            showToast('Copied to clipboard!', 'success');
        } catch (err) {
            showToast('Failed to copy', 'danger');
        }
        document.body.removeChild(textArea);
    }
}

/**
 * Initialize tooltips
 */
function initTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Initialize popovers
 */
function initPopovers() {
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

/**
 * Get URL parameters
 */
function getUrlParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

/**
 * Scroll to top
 */
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

/**
 * Format file size
 */
function formatFileSize(bytes) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
}

/**
 * Generate random ID
 */
function generateId(prefix = 'id') {
    return prefix + '-' + Math.random().toString(36).substr(2, 9);
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    // Initialize Bootstrap components
    initTooltips();
    initPopovers();
    
    // Add scroll to top button
    const scrollBtn = document.createElement('button');
    scrollBtn.innerHTML = '<i class="bi bi-arrow-up"></i>';
    scrollBtn.className = 'btn btn-primary position-fixed bottom-0 end-0 m-4 rounded-circle';
    scrollBtn.style.display = 'none';
    scrollBtn.style.zIndex = '9999';
    scrollBtn.style.width = '50px';
    scrollBtn.style.height = '50px';
    scrollBtn.onclick = scrollToTop;
    document.body.appendChild(scrollBtn);
    
    // Show/hide scroll to top button
    window.addEventListener('scroll', function() {
        if (window.pageYOffset > 300) {
            scrollBtn.style.display = 'block';
        } else {
            scrollBtn.style.display = 'none';
        }
    });
});

// Export functions for use in other files
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        formatCurrency,
        formatDate,
        getCurrentUser,
        isAuthenticated,
        isAdminOrManager,
        logout,
        showToast,
        isValidEmail,
        isValidPhone,
        calculateEMI,
        debounce,
        filterTableRows,
        sortTable,
        exportTableToCSV,
        printPage,
        copyToClipboard,
        getUrlParameter
    };
}
