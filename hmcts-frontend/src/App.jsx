import { useState, useEffect } from 'react'
import './App.css'
import TaskManager from './components/TaskManager'

function App() {
  return (
    <div className="container mx-auto p-4">
      <header className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Task Management System</h1>
      </header>
      <main>
        <TaskManager />
      </main>
    </div>
  )
}

export default App
