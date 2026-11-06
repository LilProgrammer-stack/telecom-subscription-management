import { useState, useEffect, useRef } from "react";
import { useNavigate, useLocation, useSearchParams } from "react-router";

function VerifyEmailPage() {

    const location = useLocation();
    const navigate = useNavigate();

    const [searchParams] = useSearchParams();

    const token = searchParams.get("token");

    const verificationAttempted = useRef(false);

    const [verificationStatus, setVerificationStatus] = useState("loading");

    useEffect(() => {

        const verifyEmail = async () => {
            try {
                const response = await fetch(
                    `http://localhost:8080/api/registration/verify?token=${encodeURIComponent(token)}`,
                    {
                        method: "POST"
                    }
                );

                if (!response.ok) {
                    throw new Error("Email verification failed.");
                }

                setVerificationStatus("success");

            } catch (error) {
                console.error("Verification error:", error);
                setVerificationStatus("error");
            }
        };

        if (!token) {
            setVerificationStatus("waiting");
            return;
        }

        if (verificationAttempted.current) {
            return;
        }

        verificationAttempted.current = true;
        verifyEmail();

    }, [token]);

    return (


        <div className=" bg-[#F4F8FC] pt-10">

            <div className="w-custom-layout m-auto bg-white min-h-screen flex flex-col items-center">
                <div className="bg-[#BABAFF] rounded-full p-4 mt-14">
                    <svg xmlns="http://www.w3.org/2000/svg" width="85" height="85" viewBox="0 0 24 24" fill="none" stroke="#2113F0" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-mail-check preview-icon"><path d="M22 13V6a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2v12c0 1.1.9 2 2 2h8" /><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7" /><path d="m16 19 2 2 4-4" /></svg>
                </div>

                <div className="text-3xl font-bold mb-4 mt-6">
                    <h1>
                        {verificationStatus === "waiting" && "Verify your email address"}
                        {verificationStatus === "loading" && "Verifying your email address..."}
                        {verificationStatus === "success" && "Email successfully verified!"}
                        {verificationStatus === "error" && "Email verification failed"}
                    </h1>
                </div>

                <div className="text-center ">

                    {verificationStatus === "loading" && (
                        <p>Please wait while we verify your email address.</p>
                    )}

                    {verificationStatus === "success" && (
                        <p>Your email address has been successfully verified.</p>
                    )}

                    {verificationStatus === "waiting" && (
                        <p>
                            We've sent a verification link to your email address.
                            Please check your inbox and click the link to verify your account.
                        </p>
                    )}

                    <div className="border border-blue-500 text-blue-500 rounded-lg p-2 my-4 font-semibold hover:bg-blue-500 hover:text-white transition-colors duration-300">
                        <button className="cursor-pointer ">
                            Resend verification email
                        </button>
                    </div>

                    <div className="text-blue-500 p-2 font-semibold hover:text-black transition-colors duration-300">
                        <button className="cursor-pointer" onClick={() => navigate('/register/confirm', {
                            state: location.state
                        })}>
                            Change email address
                        </button>
                    </div>


                </div>
            </div>

        </div>
    )
}

export default VerifyEmailPage