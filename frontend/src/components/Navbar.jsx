function Navbar() {
  return (
    <nav className="flex items-center justify-between bg-white px-8 py-4 shadow-md">
      <h1 className="text-2xl font-bold text-blue-700">
        CommonNet
      </h1>

      <div className="flex w-96 justify-between">
        <a className="font-semibold border-b-2 border-transparent hover:border-blue-700 hover:text-blue-700" href="">
          Home
        </a>
        <a className="font-semibold border-b-2 border-transparent hover:border-blue-700 hover:text-blue-700" href="about">
          Plan
        </a>
        <a className="font-semibold border-b-2 border-transparent hover:border-blue-700 hover:text-blue-700" href="services">
          Services
        </a>
        <a className="font-semibold border-b-2 border-transparent hover:border-blue-700 hover:text-blue-700" href="contact">
          Support
        </a>
      </div>

      <div className="flex gap-5">
        <button className="border border-blue-700 text-blue-700 px-3 py-1 cursor-pointer">
          Login
        </button>

        <button className="bg-blue-700 px-3 py-1 text-white cursor-pointer">
          Get Started
        </button>
      </div>

    </nav>

  )
}

export default Navbar