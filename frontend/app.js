const API_URL = 'http://localhost:8080/api';
const TASKS_URL = `${API_URL}/tasks`;
const USERS_URL = `${API_URL}/users`;

// Context
let currentUser = JSON.parse(localStorage.getItem('user'));

// DOM Check
const isAuthPage = document.body.classList.contains('auth-body');

// Init
document.addEventListener('DOMContentLoaded', () => {
    if (isAuthPage) {
        initAuthPages();
    } else {
        if (!currentUser) {
            window.location.href = 'login.html';
            return;
        }
        initDashboard();
    }
});

// --- Auth Logic ---
function initAuthPages() {
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            try {
                const res = await fetch(`${USERS_URL}/login`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ email, password })
                });

                if (res.ok) {
                    const user = await res.json();
                    localStorage.setItem('user', JSON.stringify(user));
                    window.location.href = 'index.html';
                } else {
                    showError('Invalid email or password');
                }
            } catch (err) {
                showError('Login failed. Server error?');
            }
        });
    }

    if (signupForm) {
        signupForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('username').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            try {
                const res = await fetch(`${USERS_URL}/register`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, email, password })
                });

                if (res.ok) {
                    alert('Registration successful! Please login.');
                    window.location.href = 'login.html';
                } else {
                    const data = await res.json();
                    showError(data.message || 'Registration failed');
                }
            } catch (err) {
                showError('Registration failed. Server error?');
            }
        });
    }
}

function showError(msg) {
    const errDiv = document.getElementById('errorMsg') || createErrorToast();
    errDiv.textContent = msg;
    errDiv.style.opacity = 1;
    setTimeout(() => { errDiv.style.opacity = 0; }, 3000);
}

function createErrorToast() {
    const div = document.createElement('div');
    div.id = 'errorMsg';
    div.className = 'error-toast';
    document.querySelector('.auth-card').appendChild(div);
    return div;
}

// --- Dashboard Logic ---
function initDashboard() {
    // Update Welcome Message
    const userDisplay = document.getElementById('userDisplay');
    if (userDisplay) userDisplay.textContent = `Hello, ${currentUser.username}`;

    // Event Listeners
    document.getElementById('logoutBtn').addEventListener('click', logout);
    document.getElementById('addTaskBtn').addEventListener('click', () => openModal());
    document.querySelector('.close').addEventListener('click', closeModal);
    document.getElementById('cancelBtn').addEventListener('click', closeModal);
    document.getElementById('taskForm').addEventListener('submit', handleTaskSubmit);

    fetchTasks();
}

function logout() {
    localStorage.removeItem('user');
    window.location.href = 'login.html';
}

async function fetchTasks() {
    try {
        const res = await fetch(`${TASKS_URL}?userId=${currentUser.id}`);
        if (!res.ok) throw new Error('Failed to fetch');
        const tasks = await res.json();
        renderTasks(tasks);
        checkDeadlines(tasks);
    } catch (err) {
        console.error(err);
    }
}

function renderTasks(tasks) {
    const list = document.getElementById('taskList');
    list.innerHTML = '';

    if (tasks.length === 0) {
        list.innerHTML = `<div style="text-align:center; color:white;">No tasks found. Add one!</div>`;
        return;
    }

    // Backend already sorts by Priority & Deadline, simply render.
    tasks.forEach(task => {
        const isUrgent = checkUrgency(task.deadline);
        const card = document.createElement('div');
        card.className = `task-card priority-${task.priority}`;
        card.innerHTML = `
            <div class="task-content">
                <h3>${escapeHtml(task.title)}</h3>
                <p>${escapeHtml(task.description || '')}</p>
                <div class="task-meta">
                    <span>${task.priority}</span>
                    <span>${task.status}</span>
                    <span>📅 ${task.deadline || 'None'}</span>
                    ${isUrgent && task.status === 'PENDING' ? '<span class="deadline-alert">⚠️ Due Soon!</span>' : ''}
                </div>
            </div>
            <div class="task-actions">
                <button class="btn-icon" onclick="openModal(${JSON.stringify(task).replace(/"/g, '&quot;')})">✏️</button>
                <button class="btn-icon" style="color:red" onclick="deleteTask(${task.id})">🗑️</button>
            </div>
        `;
        list.appendChild(card);
    });
}

function checkUrgency(deadlineStr) {
    if (!deadlineStr) return false;
    const deadline = new Date(deadlineStr);
    const today = new Date();
    const diffTime = deadline - today;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays >= 0 && diffDays <= 2; // Urgent if due within 2 days
}

function checkDeadlines(tasks) {
    const alertArea = document.getElementById('notificationArea');
    alertArea.innerHTML = '';

    const urgentTasks = tasks.filter(t => t.status === 'PENDING' && checkUrgency(t.deadline));

    urgentTasks.forEach(t => {
        const toast = document.createElement('div');
        toast.className = 'toast';
        toast.innerHTML = `<span>⚠️ Task <b>${escapeHtml(t.title)}</b> is due on ${t.deadline}!</span>`;
        alertArea.appendChild(toast);

        // Auto remove after 5s
        setTimeout(() => toast.remove(), 5000);
    });
}

async function handleTaskSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('taskId').value;
    const taskData = {
        title: document.getElementById('title').value,
        description: document.getElementById('description').value,
        deadline: document.getElementById('deadline').value,
        priority: document.getElementById('priority').value,
        status: document.getElementById('status').value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${TASKS_URL}/${id}` : `${TASKS_URL}?userId=${currentUser.id}`;

    try {
        const res = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(taskData)
        });
        if (res.ok) {
            closeModal();
            fetchTasks();
        } else {
            alert('Failed to save task');
        }
    } catch (err) {
        alert('Error saving task');
    }
}

// Global scope for HTML onclick
window.openModal = function (task = null) {
    const modal = document.getElementById('taskModal');
    const titleEle = document.getElementById('modalTitle');

    if (task) {
        titleEle.textContent = 'Edit Task';
        document.getElementById('taskId').value = task.id;
        document.getElementById('title').value = task.title;
        document.getElementById('description').value = task.description;
        document.getElementById('deadline').value = task.deadline;
        document.getElementById('priority').value = task.priority;
        document.getElementById('status').value = task.status;
    } else {
        titleEle.textContent = 'New Task';
        document.getElementById('taskForm').reset();
        document.getElementById('taskId').value = '';
    }
    modal.classList.add('show');
}

window.closeModal = function () {
    document.getElementById('taskModal').classList.remove('show');
}

window.deleteTask = async function (id) {
    if (!confirm('Delete this task?')) return;
    try {
        await fetch(`${TASKS_URL}/${id}`, { method: 'DELETE' });
        fetchTasks();
    } catch (err) {
        console.error(err);
    }
}

function escapeHtml(text) {
    if (!text) return '';
    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
