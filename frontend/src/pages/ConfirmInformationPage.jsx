import { useState } from "react";
import { useLocation, useNavigate } from "react-router";

function ConfirmInformationPage() {

    const location = useLocation();
    const navigate = useNavigate();
    const [isEditingFirstName, setIsEditingFirstName] = useState(false);
    const [firstName, setFirstName] = useState(
        location.state?.firstName ?? "John"
    );

    const [isEditingLastName, setIsEditingLastName] = useState(false);
    const [lastName, setLastName] = useState(
        location.state?.lastName ?? "Doe"
    );

    const [isEditingEmail, setIsEditingEmail] = useState(false);
    const [email, setEmail] = useState(
        location.state?.email ?? "john.doe@example.com"
    );

    const [isEditingDateOfBirth, setIsEditingDateOfBirth] = useState(false);
    const [dateOfBirth, setDateOfBirth] = useState(
        location.state?.dateOfBirth ?? "1990-01-01"
    );

    const [isEditingPassword, setIsEditingPassword] = useState(false);
    const [password, setPassword] = useState(
        location.state?.password ?? "password123"
    );

    const [isEditingConfirmPassword, setIsEditingConfirmPassword] = useState(false);
    const [confirmPassword, setConfirmPassword] = useState(
        location.state?.confirmPassword ?? "password123"
    );

    const [billingLanguage, setBillingLanguage] = useState("ENGLISH");

    const [isSubmitting, setIsSubmitting] = useState(false);

    const [errorMessage, setErrorMessage] = useState("");

    const updatedFormData = {
        customer: {
            firstName,
            lastName,
            email,
            dateOfBirth
        },
        credentials: {
            password,
            confirmPassword
        },
        account: {
            billingLanguage
        }
    };

    const registerCustomer = async () => {

        try {

            const response = await fetch(
                "http://localhost:8080/api/customer-registrations",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(updatedFormData)
                }
            );

            if (!response.ok) {
                throw new Error(`Registration failed. Status: ${response.status}`);
            }

            const data = await response.json();

            return data;

        } catch (error) {
            console.error("Registration failed:", error);
            throw error; // Rethrow the error to be handled by the caller
        }

    };

    const handleContinue = async (event) => {
        event.preventDefault();

        if (isSubmitting) return;

        setIsSubmitting(true);
        setErrorMessage("");

        try {
            const registrationResponse = await registerCustomer();

            navigate('/register/verify-email', {
                state: {
                    email: registrationResponse.email
                }
            });

        } catch (error) {
            setErrorMessage(error.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleBack = () => {
        navigate('/register', {
            state: {
                firstName,
                lastName,
                email,
                dateOfBirth,
                password,
                confirmPassword
            }
        });
    };

    return (
        <div className="pt-10 min-h-screen bg-[#F4F8FC]">
            <form
                className="w-custom-layout mx-auto bg-white shadow-md p-7"
                action=""
                onSubmit={handleContinue}
            >
                <div className="text-3xl font-semibold text-center mb-4">
                    <h1>Review your information</h1>
                </div>

                <fieldset>

                    <legend className="text-[#87817E] mb-4">
                        Please make sure your information is correct before continuing.
                    </legend>

                    <div className="flex flex-col gap-2 mb-4 border rounded-md p-3">
                        <label className="font-semibold">First name</label>

                        {isEditingFirstName ? (
                            <div className="flex gap-2">
                                <input
                                    className="border border-[#87817E] rounded-md p-1.5"
                                    type="text"
                                    value={firstName}
                                    onChange={(event) => setFirstName(event.target.value)}
                                />

                                <div className="flex items-center gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-save-check preview-icon"><path d="M12.5 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h10.2a2 2 0 0 1 1.4.6l3.8 3.8a2 2 0 0 1 .6 1.4v4.35" /><path d="m16 19 2 2 4-4" /><path d="M17 15.13V14a1 1 0 0 0-1-1H8a1 1 0 0 0-1 1v7" /><path d="M7 3v4a1 1 0 0 0 1 1h7" /></svg>

                                    <button
                                        className="cursor-pointer hover:text-blue-700 font-semibold"
                                        type="button"
                                        onClick={() => setIsEditingFirstName(false)}
                                    >
                                        Save
                                    </button>
                                </div>

                            </div>
                        ) : (
                            <div className="flex items-center justify-between">
                                <p>{firstName}</p>

                                <div className="flex gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pencil-line preview-icon"><path d="M13 21h8" /><path d="m15 5 4 4" /><path d="M21.174 6.812a1 1 0 0 0-3.986-3.987L3.842 16.174a2 2 0 0 0-.5.83l-1.321 4.352a.5.5 0 0 0 .623.622l4.353-1.32a2 2 0 0 0 .83-.497z" /></svg>

                                    <button
                                        type="button"
                                        onClick={() => setIsEditingFirstName(true)}
                                        className="cursor-pointer hover:text-blue-700 font-semibold">
                                        Edit
                                    </button>
                                </div>

                            </div>
                        )}
                    </div>



                    <div className="flex flex-col gap-2 mb-4 border rounded-md p-3">
                        <label className="font-semibold">Last name</label>

                        {isEditingLastName ? (
                            <div className="flex gap-2">
                                <input
                                    className="border border-[#87817E] rounded-md p-1.5"
                                    type="text"
                                    value={lastName}
                                    onChange={(event) => setLastName(event.target.value)}
                                />

                                <div className="flex items-center gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-save-check preview-icon"><path d="M12.5 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h10.2a2 2 0 0 1 1.4.6l3.8 3.8a2 2 0 0 1 .6 1.4v4.35" /><path d="m16 19 2 2 4-4" /><path d="M17 15.13V14a1 1 0 0 0-1-1H8a1 1 0 0 0-1 1v7" /><path d="M7 3v4a1 1 0 0 0 1 1h7" /></svg>

                                    <button
                                        className="cursor-pointer hover:text-blue-700 font-semibold"
                                        type="button"
                                        onClick={() => setIsEditingLastName(false)}
                                    >
                                        Save
                                    </button>
                                </div>

                            </div>
                        ) : (
                            <div className="flex items-center justify-between">
                                <p>{lastName}</p>

                                <div className="flex gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pencil-line preview-icon"><path d="M13 21h8" /><path d="m15 5 4 4" /><path d="M21.174 6.812a1 1 0 0 0-3.986-3.987L3.842 16.174a2 2 0 0 0-.5.83l-1.321 4.352a.5.5 0 0 0 .623.622l4.353-1.32a2 2 0 0 0 .83-.497z" /></svg>

                                    <button
                                        type="button"
                                        onClick={() => setIsEditingLastName(true)}
                                        className="cursor-pointer hover:text-blue-700 font-semibold">
                                        Edit
                                    </button>
                                </div>

                            </div>
                        )}
                    </div>



                    <div className="flex flex-col gap-2 mb-4 border rounded-md p-3">
                        <label className="font-semibold">Email</label>

                        {isEditingEmail ? (
                            <div className="flex gap-2">
                                <input
                                    className="border border-[#87817E] rounded-md p-1.5"
                                    type="text"
                                    value={email}
                                    onChange={(event) => setEmail(event.target.value)}
                                />

                                <div className="flex items-center gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-save-check preview-icon"><path d="M12.5 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h10.2a2 2 0 0 1 1.4.6l3.8 3.8a2 2 0 0 1 .6 1.4v4.35" /><path d="m16 19 2 2 4-4" /><path d="M17 15.13V14a1 1 0 0 0-1-1H8a1 1 0 0 0-1 1v7" /><path d="M7 3v4a1 1 0 0 0 1 1h7" /></svg>

                                    <button
                                        className="cursor-pointer hover:text-blue-700 font-semibold"
                                        type="button"
                                        onClick={() => setIsEditingEmail(false)}
                                    >
                                        Save
                                    </button>
                                </div>

                            </div>
                        ) : (
                            <div className="flex items-center justify-between">
                                <p>{email}</p>

                                <div className="flex gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pencil-line preview-icon"><path d="M13 21h8" /><path d="m15 5 4 4" /><path d="M21.174 6.812a1 1 0 0 0-3.986-3.987L3.842 16.174a2 2 0 0 0-.5.83l-1.321 4.352a.5.5 0 0 0 .623.622l4.353-1.32a2 2 0 0 0 .83-.497z" /></svg>

                                    <button
                                        type="button"
                                        onClick={() => setIsEditingEmail(true)}
                                        className="cursor-pointer hover:text-blue-700 font-semibold">
                                        Edit
                                    </button>
                                </div>

                            </div>
                        )}
                    </div>



                    <div className="flex flex-col gap-2 mb-4 border rounded-md p-3">
                        <label className="font-semibold">Date of Birth</label>

                        {isEditingDateOfBirth ? (
                            <div className="flex gap-2">
                                <input
                                    className="border border-[#87817E] rounded-md p-1.5"
                                    type="text"
                                    value={dateOfBirth}
                                    onChange={(event) => setDateOfBirth(event.target.value)}
                                />

                                <div className="flex items-center gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-save-check preview-icon"><path d="M12.5 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h10.2a2 2 0 0 1 1.4.6l3.8 3.8a2 2 0 0 1 .6 1.4v4.35" /><path d="m16 19 2 2 4-4" /><path d="M17 15.13V14a1 1 0 0 0-1-1H8a1 1 0 0 0-1 1v7" /><path d="M7 3v4a1 1 0 0 0 1 1h7" /></svg>

                                    <button
                                        className="cursor-pointer hover:text-blue-700 font-semibold"
                                        type="button"
                                        onClick={() => setIsEditingDateOfBirth(false)}
                                    >
                                        Save
                                    </button>
                                </div>

                            </div>
                        ) : (
                            <div className="flex items-center justify-between">
                                <p>{dateOfBirth}</p>

                                <div className="flex gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pencil-line preview-icon"><path d="M13 21h8" /><path d="m15 5 4 4" /><path d="M21.174 6.812a1 1 0 0 0-3.986-3.987L3.842 16.174a2 2 0 0 0-.5.83l-1.321 4.352a.5.5 0 0 0 .623.622l4.353-1.32a2 2 0 0 0 .83-.497z" /></svg>

                                    <button
                                        type="button"
                                        onClick={() => setIsEditingDateOfBirth(true)}
                                        className="cursor-pointer hover:text-blue-700 font-semibold">
                                        Edit
                                    </button>
                                </div>

                            </div>
                        )}
                    </div>



                    <div className="flex flex-col gap-2 mb-4 border rounded-md p-3">
                        <label className="font-semibold">Password</label>

                        {isEditingPassword ? (
                            <div className="flex gap-2">
                                <input
                                    className="border border-[#87817E] rounded-md p-1.5"
                                    type="password"
                                    value={password}
                                    onChange={(event) => setPassword(event.target.value)}
                                />

                                <div className="flex items-center gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-save-check preview-icon"><path d="M12.5 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h10.2a2 2 0 0 1 1.4.6l3.8 3.8a2 2 0 0 1 .6 1.4v4.35" /><path d="m16 19 2 2 4-4" /><path d="M17 15.13V14a1 1 0 0 0-1-1H8a1 1 0 0 0-1 1v7" /><path d="M7 3v4a1 1 0 0 0 1 1h7" /></svg>

                                    <button
                                        className="cursor-pointer hover:text-blue-700 font-semibold"
                                        type="button"
                                        onClick={() => setIsEditingPassword(false)}
                                    >
                                        Save
                                    </button>
                                </div>

                            </div>
                        ) : (
                            <div className="flex items-center justify-between">
                                <p>••••••••</p>

                                <div className="flex gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pencil-line preview-icon"><path d="M13 21h8" /><path d="m15 5 4 4" /><path d="M21.174 6.812a1 1 0 0 0-3.986-3.987L3.842 16.174a2 2 0 0 0-.5.83l-1.321 4.352a.5.5 0 0 0 .623.622l4.353-1.32a2 2 0 0 0 .83-.497z" /></svg>

                                    <button
                                        type="button"
                                        onClick={() => setIsEditingPassword(true)}
                                        className="cursor-pointer hover:text-blue-700 font-semibold">
                                        Edit
                                    </button>
                                </div>

                            </div>
                        )}
                    </div>



                    <div className="flex flex-col gap-2 mb-4 border rounded-md p-3">
                        <label className="font-semibold">Confirm Password</label>

                        {isEditingConfirmPassword ? (
                            <div className="flex gap-2">
                                <input
                                    className="border border-[#87817E] rounded-md p-1.5"
                                    type="password"
                                    value={confirmPassword}
                                    onChange={(event) => setConfirmPassword(event.target.value)}
                                />

                                <div className="flex items-center gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-save-check preview-icon"><path d="M12.5 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h10.2a2 2 0 0 1 1.4.6l3.8 3.8a2 2 0 0 1 .6 1.4v4.35" /><path d="m16 19 2 2 4-4" /><path d="M17 15.13V14a1 1 0 0 0-1-1H8a1 1 0 0 0-1 1v7" /><path d="M7 3v4a1 1 0 0 0 1 1h7" /></svg>

                                    <button
                                        className="cursor-pointer hover:text-blue-700 font-semibold"
                                        type="button"
                                        onClick={() => setIsEditingConfirmPassword(false)}
                                    >
                                        Save
                                    </button>
                                </div>

                            </div>
                        ) : (
                            <div className="flex items-center justify-between">
                                <p>••••••••</p>

                                <div className="flex gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pencil-line preview-icon"><path d="M13 21h8" /><path d="m15 5 4 4" /><path d="M21.174 6.812a1 1 0 0 0-3.986-3.987L3.842 16.174a2 2 0 0 0-.5.83l-1.321 4.352a.5.5 0 0 0 .623.622l4.353-1.32a2 2 0 0 0 .83-.497z" /></svg>

                                    <button
                                        type="button"
                                        onClick={() => setIsEditingConfirmPassword(true)}
                                        className="cursor-pointer hover:text-blue-700 font-semibold">
                                        Edit
                                    </button>
                                </div>

                            </div>
                        )}
                    </div>

                    <div className="flex justify-between border rounded-md mb-4 p-3">

                        <div>
                            <p className="font-semibold">
                                What is your preferred billing language?
                            </p>
                            <p className="font-semibold">
                                ¿Cuál es su idioma preferido para la facturación?
                            </p>
                        </div>

                        <select
                            name="billingLanguage"
                            value={billingLanguage}
                            onChange={(e) => setBillingLanguage(e.target.value)}>
                            <option className="text-gray-700" value="ENGLISH">English</option>
                            <option className="text-gray-700" value="SPANISH">Spanish</option>
                        </select>
                    </div>

                    {errorMessage && (
                        <p className="text-red-600 text-sm mb-4" role="alert">
                            {errorMessage}
                        </p>
                    )}

                    <div className="bg-blue-700 text-center p-3 rounded-md text-white font-semibold hover:bg-blue-800">
                        <button
                            className="cursor-pointer w-full"
                            type="submit"
                            disabled={isSubmitting}
                        >
                            {isSubmitting ? "Creating account..." : "Continue"}
                        </button>
                    </div>

                    <div className="text-center mt-4 text-blue-700 font-semibold">
                        <button
                            className="cursor-pointer hover:text-black transition-colors duration-300"
                            type="button"
                            onClick={handleBack}
                        >
                            Back
                        </button>

                    </div>

                </fieldset>




            </form>
        </div>

    )
}

export default ConfirmInformationPage