import { useState, useEffect } from 'react';
import TaskList from './TaskList';
import TaskForm from './TaskForm';

const TaskManager = () => {
  const [tasks, setTasks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedTask, setSelectedTask] = useState(null);
  const [isEditing, setIsEditing] = useState(false);

  // API base URL
  const API_URL = 'http://localhost:8080/task';

  // Fetch all tasks
  const fetchTasks = async () => {
    setIsLoading(true);
    try {
      const response = await fetch(API_URL);
      if (!response.ok) {
        throw new Error('Failed to fetch tasks');
      }
      const data = await response.json();
      setTasks(data);
      setError(null);
    } catch (error) {
      setError('Error fetching tasks: ' + error.message);
      console.error('Error fetching tasks:', error);
    } finally {
      setIsLoading(false);
    }
  };

  // Create a new task
  const createTask = async (taskData) => {
    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
      });
      
      if (!response.ok) {
        throw new Error('Failed to create task');
      }
      
      const newTask = await response.json();
      setTasks([...tasks, newTask]);
      return true;
    } catch (error) {
      setError('Error creating task: ' + error.message);
      console.error('Error creating task:', error);
      return false;
    }
  };

  // Update an existing task
  const updateTask = async (taskId, taskData) => {
    try {
      const response = await fetch(`${API_URL}/${taskId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
      });
      
      if (!response.ok) {
        throw new Error('Failed to update task');
      }
      
      const updatedTask = await response.json();
      setTasks(tasks.map(task => 
        task.taskId === taskId ? updatedTask : task
      ));
      return true;
    } catch (error) {
      setError('Error updating task: ' + error.message);
      console.error('Error updating task:', error);
      return false;
    }
  };

  // Update task status only
  const updateTaskStatus = async (taskId, status) => {
    try {
      const response = await fetch(`${API_URL}/${taskId}/status`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(status),
      });
      
      if (!response.ok) {
        throw new Error('Failed to update task status');
      }
      
      const updatedTask = await response.json();
      setTasks(tasks.map(task => 
        task.taskId === taskId ? updatedTask : task
      ));
      return true;
    } catch (error) {
      setError('Error updating task status: ' + error.message);
      console.error('Error updating task status:', error);
      return false;
    }
  };

  // Delete a task
  const deleteTask = async (taskId) => {
    try {
      const response = await fetch(`${API_URL}/${taskId}`, {
        method: 'DELETE',
      });
      
      if (!response.ok) {
        throw new Error('Failed to delete task');
      }
      
      setTasks(tasks.filter(task => task.taskId !== taskId));
      return true;
    } catch (error) {
      setError('Error deleting task: ' + error.message);
      console.error('Error deleting task:', error);
      return false;
    }
  };

  // Edit task handler
  const handleEditTask = (task) => {
    setSelectedTask(task);
    setIsEditing(true);
  };

  // Cancel edit handler
  const handleCancelEdit = () => {
    setSelectedTask(null);
    setIsEditing(false);
  };

  // Effect to load tasks on component mount
  useEffect(() => {
    fetchTasks();
  }, []);

  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div className="md:col-span-2">
        <h2 className="text-xl font-semibold mb-4">Tasks</h2>
        {isLoading ? (
          <p className="text-gray-600">Loading tasks...</p>
        ) : error ? (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {error}
          </div>
        ) : (
          <TaskList 
            tasks={tasks} 
            onDelete={deleteTask} 
            onEdit={handleEditTask}
            onStatusChange={updateTaskStatus}
          />
        )}
      </div>
      <div className="bg-gray-50 p-4 rounded-lg">
        <h2 className="text-xl font-semibold mb-4">
          {isEditing ? 'Edit Task' : 'Add New Task'}
        </h2>
        <TaskForm 
          onSubmit={isEditing ? 
            (taskData) => updateTask(selectedTask.taskId, taskData) : 
            createTask
          }
          initialData={selectedTask}
          onCancel={isEditing ? handleCancelEdit : null}
          isEditing={isEditing}
        />
      </div>
    </div>
  );
};

export default TaskManager; 