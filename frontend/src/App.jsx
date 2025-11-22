import React, { useState, useContext } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

/* Import the page components */
import { Login, CompleteLogin } from './pages/Login.jsx';
import StudentTimetable from './pages/StudentTimetable.jsx';
import LecturerTimetable from './pages/LecturerTimetable.jsx';
import { AuthContext, AuthProvider } from './context/Contexts.jsx';


/* Separated out BrowserRouter into a separate function with a given authContext */
const AppRouter = () => {
    /* Auth context */
    const authContext = useContext(AuthContext);

    /* Returning router code */
    return (
        <BrowserRouter>
            <Routes>
                {/* Route for the Authentication Page.
                  If logged in, redirects to the lecturer timetable. Otherwise, shows the Auth component.
                */}
                <Route
                    path="/"
                    element={authContext.auth
                        ? <Navigate to={authContext.auth.isLecturer ? "/lecturer-timetable" : "/timetable"} replace />
                        : <Login handleLogin={authContext.login} />
                    }
                />

                {/* Route for the 'signing in...' page, also processes the actual login */}
                <Route
                    path="/auth-callback"
                    element={authContext.auth
                        ? <Navigate to="/" replace />
                        : <CompleteLogin handleLoginCallback={authContext.loginCallback} />
                    }
                />

                {/* Route for the Student Timetable Page.
                  If not logged in, redirects to the root (Auth) page.
                */}
                <Route
                    path="/timetable"
                    element={(authContext.auth && !authContext.auth.isLecturer)
                        ? <StudentTimetable onLogout={authContext.logout} />
                        : <Navigate to="/" replace />
                    }
                />

                {/* Route for the Lecturer Timetable Page */}
                <Route
                    path="/lecturer-timetable"
                    element={(authContext.auth && authContext.auth.isLecturer)
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

const App = () => {
    /* Wrapper for the actual app taking care of the authentication context */
    return (
        <AuthProvider>
            <AppRouter/>
        </AuthProvider>
    );
};

export default App;
