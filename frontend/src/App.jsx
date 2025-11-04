import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

// Import the page components
import Auth from './pages/Auth.jsx';
import StudentTimetable from './pages/StudentTimetable.jsx';
import LecturerTimetable from './pages/LecturerTimetable.jsx';
import LecturerCreateModule from './pages/LecturerCreateModule.jsx'

// Simple placeholder for an authentication state
const App = () => {
    // State to simulate authentication status
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const handleLogin = () => {
        setIsLoggedIn(true);
        console.log("User logged in successfully.");
    };

    const handleLogout = () => {
        setIsLoggedIn(false);
        console.log("User logged out.");
    };

    return (
        <BrowserRouter>
            <Routes>
                {/* Route for the Authentication Page.
                  If logged in, redirects to the lecturer timetable. Otherwise, shows the Auth component.
                */}
                <Route
                    path="/"
                    element={isLoggedIn
                        ? <Navigate to="/lecturer-timetable" replace /> // Changed redirection here
                        : <Auth onLoginSuccess={handleLogin} />
                    }
                />

                <Route
                    path="/createModule"
                    element={<LecturerCreateModule />}
                    />
                {/* Route for the Student Timetable Page.
                  If not logged in, redirects to the root (Auth) page.
                */}
                <Route
                    path="/timetable"
                    element={isLoggedIn
                        ? <StudentTimetable onLogout={handleLogout} />
                        : <Navigate to="/" replace />
                    }
                />

                {/* Route for the Lecturer Timetable Page */}
                <Route
                    path="/lecturer-timetable"
                    element={isLoggedIn
                        ? <LecturerTimetable />
                        : <Navigate to="/" replace />
                    }
                />

                {/* Catch-all route for 404 pages */}
                <Route path="*" element={
                    <div style={{ padding: '20px', textAlign: 'center' }}>
                        <h1>404</h1>
                        <p>Page Not Found</p>
                    </div>
                } />
            </Routes>
        </BrowserRouter>
    );
};

export default App;