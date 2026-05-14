// ===========================
// C.A.R.E - Main JavaScript File
// ===========================

document.addEventListener('DOMContentLoaded', function() {
    // Initialize all features
    initComplaintFilter();
    initViewDetails();
    initAdminDashboard();
    initModalHandlers();
    initDetailsModalHandlers();
});

// ===========================
// COMPLAINT FILTER (My Complaints Page)
// ===========================
function initComplaintFilter() {
    const filterSelect = document.getElementById('status-filter');
    const complaintsContainer = document.getElementById('complaints-container');
    const emptyState = document.getElementById('empty-state');
    
    if (!filterSelect || !complaintsContainer) return;
    
    filterSelect.addEventListener('change', function() {
        const selectedStatus = this.value;
        const complaintCards = complaintsContainer.querySelectorAll('.complaint-card-detailed');
        let visibleCount = 0;
        
        complaintCards.forEach(function(card) {
            const cardStatus = card.getAttribute('data-status');
            
            if (selectedStatus === 'all' || cardStatus === selectedStatus) {
                card.style.display = 'block';
                // Smooth fade-in animation
                card.style.opacity = '0';
                setTimeout(function() {
                    card.style.transition = 'opacity 0.3s ease';
                    card.style.opacity = '1';
                }, 10);
                visibleCount++;
            } else {
                card.style.display = 'none';
            }
        });
        
        // Show/hide empty state
        if (emptyState) {
            if (visibleCount === 0) {
                emptyState.style.display = 'block';
                complaintsContainer.style.display = 'none';
            } else {
                emptyState.style.display = 'none';
                complaintsContainer.style.display = 'flex';
            }
        }
    });
}

// ===========================
// VIEW DETAILS FUNCTIONALITY
// ===========================
function initViewDetails() {
    const viewDetailsBtns = document.querySelectorAll('.complaint-footer .btn-small');
    
    if (viewDetailsBtns.length === 0) return;
    
    viewDetailsBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            const card = this.closest('.complaint-card-detailed');
            if (!card) return;
            
            // Extract complaint data from the card
            const complaintId = card.querySelector('.complaint-id').textContent;
            const title = card.querySelector('h3').textContent;
            const status = card.getAttribute('data-status');
            const statusBadge = card.querySelector('.status-badge').className;
            
            // Extract details from complaint-body
            const bodyParagraphs = card.querySelectorAll('.complaint-body p');
            let category = '';
            let location = '';
            let priority = '';
            let date = '';
            let description = '';
            
            bodyParagraphs.forEach(function(p) {
                const text = p.textContent;
                if (text.includes('Category:')) {
                    category = text.replace('Category:', '').trim();
                } else if (text.includes('Location:')) {
                    location = text.replace('Location:', '').trim();
                } else if (text.includes('Priority:')) {
                    priority = text.replace('Priority:', '').trim();
                } else if (text.includes('Submitted on:')) {
                    date = text.replace('Submitted on:', '').trim();
                } else if (p.classList.contains('complaint-description')) {
                    description = text;
                }
            });
            
            // Mock admin response based on status
            let adminResponse = '';
            if (status === 'In Progress') {
                adminResponse = 'Your complaint has been assigned to our maintenance team. We are currently investigating the issue and will provide an update soon.';
            } else if (status === 'Resolved') {
                adminResponse = 'This issue has been resolved. Our team has completed the necessary repairs. Please let us know if you experience any further problems.';
            }
            
            // Open details modal
            openComplaintDetailsModal({
                id: complaintId,
                title: title,
                status: status,
                statusClass: statusBadge,
                category: category,
                location: location,
                priority: priority,
                date: date,
                description: description,
                adminResponse: adminResponse
            });
        });
    });
}

function openComplaintDetailsModal(data) {
    const modal = document.getElementById('complaint-details-modal');
    if (!modal) return;
    
    // Populate modal with data
    document.getElementById('detail-complaint-id').textContent = data.id;
    document.getElementById('detail-title').textContent = data.title;
    document.getElementById('detail-category').textContent = data.category;
    document.getElementById('detail-location').textContent = data.location;
    document.getElementById('detail-priority').textContent = data.priority;
    document.getElementById('detail-date').textContent = data.date;
    document.getElementById('detail-description').textContent = data.description;
    
    // Set status badge
    const statusBadge = document.getElementById('detail-status');
    statusBadge.textContent = data.status;
    statusBadge.className = data.statusClass;
    
    // Show/hide admin response
    const responseSection = document.getElementById('admin-response-section');
    const responseText = document.getElementById('detail-admin-response');
    
    if (data.adminResponse) {
        responseText.textContent = data.adminResponse;
        responseSection.style.display = 'block';
    } else {
        responseSection.style.display = 'none';
    }
    
    // Show modal
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
}

// ===========================
// DETAILS MODAL HANDLERS
// ===========================
function initDetailsModalHandlers() {
    const modal = document.getElementById('complaint-details-modal');
    if (!modal) return;
    
    const closeBtn = modal.querySelector('.modal-close');
    const closeButton = modal.querySelector('.modal-close-btn');
    
    if (closeBtn) {
        closeBtn.addEventListener('click', function() {
            closeDetailsModal();
        });
    }
    
    if (closeButton) {
        closeButton.addEventListener('click', function() {
            closeDetailsModal();
        });
    }
    
    // Click outside to close
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            closeDetailsModal();
        }
    });
    
    // ESC key to close
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && modal.classList.contains('active')) {
            closeDetailsModal();
        }
    });
}

function closeDetailsModal() {
    const modal = document.getElementById('complaint-details-modal');
    if (modal) {
        modal.classList.remove('active');
        document.body.style.overflow = 'auto';
    }
}

// ===========================
// ADMIN DASHBOARD FUNCTIONALITY
// ===========================
function initAdminDashboard() {
    const reviewButtons = document.querySelectorAll('.review-btn');
    const modal = document.getElementById('review-modal');
    
    if (!modal) return;
    
    reviewButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const row = this.closest('tr');
            openReviewModal(row);
        });
    });
}

function openReviewModal(row) {
    const modal = document.getElementById('review-modal');
    const complaintId = row.getAttribute('data-complaint-id');
    const status = row.getAttribute('data-status');
    
    // Get data from table row
    const cells = row.querySelectorAll('td');
    const id = cells[0].textContent;
    const title = cells[1].textContent;
    const student = cells[2].textContent;
    
    // Populate modal
    document.getElementById('modal-complaint-id').textContent = id;
    document.getElementById('modal-complaint-title').textContent = title;
    document.getElementById('modal-student-name').textContent = student;
    document.getElementById('modal-current-status').textContent = status;
    document.getElementById('status-select').value = status;
    document.getElementById('admin-notes').value = '';
    
    // Store row reference for updating later
    modal.setAttribute('data-current-row-id', complaintId);
    
    // Show modal
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
}

// ===========================
// MODAL HANDLERS
// ===========================
function initModalHandlers() {
    const modal = document.getElementById('review-modal');
    if (!modal) return;
    
    const closeBtn = modal.querySelector('.modal-close');
    const cancelBtn = modal.querySelector('.modal-cancel');
    const updateBtn = document.getElementById('update-status-btn');
    
    // Close modal handlers
    if (closeBtn) {
        closeBtn.addEventListener('click', function() {
            closeModal(modal);
        });
    }
    
    if (cancelBtn) {
        cancelBtn.addEventListener('click', function() {
            closeModal(modal);
        });
    }
    
    // Click outside modal to close
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            closeModal(modal);
        }
    });
    
    // Update status handler
    if (updateBtn) {
        updateBtn.addEventListener('click', function() {
            updateComplaintStatus(modal);
        });
    }
}

function closeModal(modal) {
    modal.classList.remove('active');
    document.body.style.overflow = 'auto';
}

function updateComplaintStatus(modal) {
    const complaintId = modal.getAttribute('data-current-row-id');
    const newStatus = document.getElementById('status-select').value;
    const notes = document.getElementById('admin-notes').value;
    
    // Find the row to update
    const row = document.querySelector(`tr[data-complaint-id="${complaintId}"]`);
    
    if (row) {
        // Update data attribute
        row.setAttribute('data-status', newStatus);
        
        // Update status badge in table
        const statusBadge = row.querySelector('.status-badge');
        if (statusBadge) {
            // Remove old classes
            statusBadge.classList.remove('status-open', 'status-progress', 'status-resolved');
            
            // Add new class and text
            statusBadge.textContent = newStatus;
            
            if (newStatus === 'Open') {
                statusBadge.classList.add('status-open');
            } else if (newStatus === 'In Progress') {
                statusBadge.classList.add('status-progress');
            } else if (newStatus === 'Resolved') {
                statusBadge.classList.add('status-resolved');
            }
            
            // Add animation effect
            statusBadge.style.transform = 'scale(1.1)';
            setTimeout(function() {
                statusBadge.style.transition = 'transform 0.3s ease';
                statusBadge.style.transform = 'scale(1)';
            }, 100);
        }
        
        // Update stats counters
        updateStatsCounters();
        
        // Show success feedback
        showSuccessMessage('Status updated successfully!');
    }
    
    // Close modal
    closeModal(modal);
}

// ===========================
// UPDATE STATS COUNTERS
// ===========================
function updateStatsCounters() {
    const allRows = document.querySelectorAll('#complaints-tbody tr');
    
    let openCount = 0;
    let progressCount = 0;
    let resolvedCount = 0;
    
    allRows.forEach(function(row) {
        const status = row.getAttribute('data-status');
        
        if (status === 'Open') {
            openCount++;
        } else if (status === 'In Progress') {
            progressCount++;
        } else if (status === 'Resolved') {
            resolvedCount++;
        }
    });
    
    const totalCount = allRows.length;
    
    // Update counter elements with animation
    animateCounter('open-count', openCount);
    animateCounter('progress-count', progressCount);
    animateCounter('resolved-count', resolvedCount);
    animateCounter('total-count', totalCount);
}

function animateCounter(elementId, targetValue) {
    const element = document.getElementById(elementId);
    if (!element) return;
    
    const currentValue = parseInt(element.textContent) || 0;
    const increment = targetValue > currentValue ? 1 : -1;
    const duration = 500; // milliseconds
    const steps = Math.abs(targetValue - currentValue);
    const stepDuration = steps > 0 ? duration / steps : 0;
    
    let current = currentValue;
    
    const timer = setInterval(function() {
        current += increment;
        element.textContent = current;
        
        if ((increment > 0 && current >= targetValue) || (increment < 0 && current <= targetValue)) {
            element.textContent = targetValue;
            clearInterval(timer);
        }
    }, stepDuration);
}

// ===========================
// SUCCESS MESSAGE
// ===========================
function showSuccessMessage(message) {
    // Create success message element
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.textContent = message;
    successDiv.style.cssText = `
        position: fixed;
        top: 100px;
        right: 20px;
        background: linear-gradient(135deg, #10b981 0%, #059669 100%);
        color: white;
        padding: 1rem 1.5rem;
        border-radius: 10px;
        box-shadow: 0 8px 25px rgba(16, 185, 129, 0.4);
        z-index: 3000;
        animation: slideInRight 0.3s ease;
        font-weight: 600;
    `;
    
    // Add to body
    document.body.appendChild(successDiv);
    
    // Remove after 3 seconds
    setTimeout(function() {
        successDiv.style.animation = 'slideOutRight 0.3s ease';
        setTimeout(function() {
            document.body.removeChild(successDiv);
        }, 300);
    }, 3000);
}

// Add CSS animations for success message
const style = document.createElement('style');
style.textContent = `
    @keyframes slideInRight {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOutRight {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// ===========================
// KEYBOARD SHORTCUTS
// ===========================
document.addEventListener('keydown', function(e) {
    const modal = document.getElementById('review-modal');
    
    // Close modal on ESC key
    if (e.key === 'Escape' && modal && modal.classList.contains('active')) {
        closeModal(modal);
    }
});

// ===========================
// SMOOTH SCROLL FOR NAVIGATION
// ===========================
document.querySelectorAll('a[href^="#"]').forEach(function(anchor) {
    anchor.addEventListener('click', function(e) {
        const href = this.getAttribute('href');
        if (href !== '#') {
            e.preventDefault();
            const target = document.querySelector(href);
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        }
    });
});

// ===========================
// CONSOLE MESSAGE
// ===========================
console.log('%cC.A.R.E System Initialized', 'color: #3b82f6; font-size: 16px; font-weight: bold;');
console.log('%cComplaint And Resolution Engine v1.0', 'color: #64748b; font-size: 12px;');
