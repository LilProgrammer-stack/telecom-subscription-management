import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router'
import { useLocation } from 'react-router';

function RegisterPage() {
    const navigate = useNavigate();
    const location = useLocation();

    const [formData, setFormData] = useState({

        firstName: location.state?.firstName ?? "",
        lastName: location.state?.lastName ?? "",
        email: location.state?.email ?? "",
        dateOfBirth: location.state?.dateOfBirth ?? "",
        password: location.state?.password ?? "",
        confirmPassword: location.state?.confirmPassword ?? ""
    })

    useEffect(() => {
        console.log("RegisterPage received:", location.state);
        if (location.state) {
            setFormData(location.state);
        }
    }, [location.state]);

    const checkPasswordLength = formData.password.length >= 10 && formData.password.length <= 255;
    const passwordHasUppercase = /[A-Z]/.test(formData.password);
    const passwordHasLowercase = /[a-z]/.test(formData.password);
    const passwordHasNumber = /[0-9]/.test(formData.password);
    const passwordHasSpecialChar = /[!@#$%^&*]/.test(formData.password);

    const checkConfirmPassword = formData.password === formData.confirmPassword;

    const [hasStartedTypingPassword, setHasStartedTypingPassword] = useState(false);


    function handleSubmit(event) {
        event.preventDefault();

        if (!checkPasswordLength) {
            alert('Password must be between 10 and 255 characters')
            return
        }

        if (!passwordHasUppercase) {
            alert('Password must contain at least one uppercase letter')
            return
        }

        if (!passwordHasLowercase) {
            alert('Password must contain at least one lowercase letter')
            return
        }

        if (!passwordHasNumber) {
            alert('Password must contain at least one number')
            return
        }

        if (!passwordHasSpecialChar) {
            alert('Password must contain at least one special character')
            return
        }

        if (!checkConfirmPassword) {
            alert('Passwords do not match')
            return
        }

        navigate('/register/confirm', {
            state: formData
        })

    }

    return (
        <div className="bg-[#F4F8FC] pt-10 min-h-screen">
            <form className="bg-white shadow-md p-7 w-custom-layout mx-auto" action="" onSubmit={handleSubmit}>

                <div>
                    <h1 className="text-3xl font-semibold text-center mb-4">Create your CommonNet Account</h1>
                </div>

                <fieldset>

                    <legend className="text-[#87817E] mb-4">Enter your personal information to get started</legend>

                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                        <div className="flex flex-col gap-1">
                            <label
                                className="font-semibold"
                                htmlFor="firstName">First Name
                            </label>
                            <input
                                className="border border-[#87817E] rounded-md p-1.5"
                                type="text"
                                id="firstName"
                                placeholder="Enter your first name"
                                value={formData.firstName}
                                onChange={(event) =>
                                    setFormData({ ...formData, firstName: event.target.value })} />
                        </div>

                        <div className="flex flex-col gap-1">
                            <label className="font-semibold" htmlFor="lastName">Last Name</label>
                            <input
                                className="border border-[#87817E] rounded-md p-1.5"
                                type="text"
                                id="lastName"
                                placeholder="Enter your last name"
                                value={formData.lastName}
                                onChange={(event) =>
                                    setFormData({ ...formData, lastName: event.target.value })} />
                        </div>
                    </div>

                    <div className="flex flex-col gap-4">
                        <div className="flex flex-col gap-1">
                            <label className="font-semibold" htmlFor="email">Email</label>
                            <input
                                className="border border-[#87817E] rounded-md p-1.5"
                                type="email" id="email"
                                placeholder="Enter your email"
                                value={formData.email}
                                onChange={(event) =>
                                    setFormData({ ...formData, email: event.target.value })} />
                        </div>

                        <div className="flex flex-col gap-1">
                            <label className="font-semibold" htmlFor="dateOfBirth">Date of Birth</label>
                            <input
                                className="border border-[#87817E] rounded-md p-1.5"
                                type="date"
                                id="dateOfBirth"
                                value={formData.dateOfBirth}
                                onChange={(event) =>
                                    setFormData({ ...formData, dateOfBirth: event.target.value })} />
                        </div>

                        <div className="flex flex-col gap-1">
                            <label className="font-semibold" htmlFor="password">Password</label>
                            <input
                                className="border border-[#87817E] rounded-md p-1.5"
                                type="password"
                                id="password"
                                placeholder="Enter your password"
                                value={formData.password}
                                onChange={(event) => {
                                    setFormData({ ...formData, password: event.target.value }); setHasStartedTypingPassword(event.target.value.length > 0);
                                }} />
                        </div>

                        <div className="flex flex-col gap-1">
                            <label className="font-semibold" htmlFor="confirmPassword">Confirm Password</label>
                            <input
                                className="border border-[#87817E] rounded-md p-1.5"
                                type="password"
                                id="confirmPassword"
                                placeholder="Confirm your password"
                                value={formData.confirmPassword}
                                onChange={(event) => {
                                    setFormData({ ...formData, confirmPassword: event.target.value });
                                }} />
                        </div>

                    </div>

                </fieldset>

                <div className="flex flex-col gap-2 sm:grid grid-cols-[7%_93%] bg-blue-100 p-8 mt-4 mb-4 rounded-md">
                    <div className="flex items-center justify-center sm:justify-start sm:items-start">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-lock-keyhole preview-icon"><circle cx="12" cy="16" r="1" /><rect x="3" y="10" width="18" height="12" rx="2" /><path d="M7 10V7a5 5 0 0 1 10 0v3" /></svg>
                    </div>
                    <div>
                        <strong>Password requirements</strong>
                        <p>Your password must:</p>

                        <ul className="flex flex-col gap-1 mt-2">

                            <li className="flex items-center gap-2">
                                <span className={
                                    !hasStartedTypingPassword ? "text-gray-400 font-bold" : checkPasswordLength ? "text-green-600 font-bold" : "text-red-600 font-bold"
                                }>
                                    {
                                        !hasStartedTypingPassword ? '•'
                                            : checkPasswordLength ? '✓' : '✗'
                                    }

                                </span>
                                Be between 10 and 255 characters
                            </li>

                            <li className="flex items-center gap-2">
                                <span className={
                                    !hasStartedTypingPassword ? "text-gray-400 font-bold" : (passwordHasUppercase ? "text-green-600 font-bold" : "text-red-600 font-bold")
                                }>
                                    {
                                        !hasStartedTypingPassword ? '•'
                                            : passwordHasUppercase ? '✓' : '✗'
                                    }
                                </span>
                                Include at least one uppercase letter (A–Z)
                            </li>
                            <li className="flex items-center gap-2">
                                <span className={
                                    !hasStartedTypingPassword ? "text-gray-400 font-bold" : (passwordHasLowercase ? "text-green-600 font-bold" : "text-red-600 font-bold")
                                }>
                                    {
                                        !hasStartedTypingPassword ? '•'
                                            : passwordHasLowercase ? '✓' : '✗'
                                    }
                                </span>
                                Include at least one lowercase letter (a–z)
                            </li>
                            <li className="flex items-center gap-2">
                                <span className={
                                    !hasStartedTypingPassword ? "text-gray-400 font-bold" : (passwordHasNumber ? "text-green-600 font-bold" : "text-red-600 font-bold")
                                }>
                                    {
                                        !hasStartedTypingPassword ? '•'
                                            : passwordHasNumber ? '✓' : '✗'
                                    }
                                </span>
                                Include at least one number (0–9)
                            </li>
                            <li className="flex items-center gap-2">
                                <span className={
                                    !hasStartedTypingPassword ? "text-gray-400 font-bold" : (passwordHasSpecialChar ? "text-green-600 font-bold" : "text-red-600 font-bold")
                                }>
                                    {
                                        !hasStartedTypingPassword ? '•'
                                            : passwordHasSpecialChar ? '✓' : '✗'
                                    }
                                </span>
                                Include at least one special character (e.g. ! @ # $ % ^ & *)
                            </li>
                        </ul>

                    </div>
                </div>

                <button className="w-full bg-blue-700 pt-2 pb-2 text-center text-white font-semibold rounded-md cursor-pointer hover:bg-blue-800" type="submit">Register</button>

                <p className="text-center mt-4">
                    Already have an account? <a className="text-blue-700" href="/login">Login</a>
                </p>

            </form>
        </div>
    )

}

export default RegisterPage