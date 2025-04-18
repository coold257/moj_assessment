import { useState, useEffect } from 'react';

const TaskForm = ({ onSubmit, initialData, onCancel, isEditing }) => {
  const [formData, setFormData] = useState({
    taskTitle: '',
    taskDescription: '',
    taskStatus: 'TODO',
    dueDateTime: ''
  });
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Update form data when editing a task
  useEffect(() => {
    if (initialData) {
      setFormData({
        taskTitle: initialData.taskTitle || '',
        taskDescription: initialData.taskDescription || '',
        taskStatus: initialData.taskStatus || 'TODO',
        dueDateTime: initialData.dueDateTime ? new Date(initialData.dueDateTime).toISOString().slice(0, 16) : ''
      });
    } else {
      // Reset form when creating a new task
      setFormData({
        taskTitle: '',
        taskDescription: '',
        taskStatus: 'TODO',
        dueDateTime: ''
      });
    }
  }, [initialData]);

  // Handle input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    // Validate form
    if (!formData.taskTitle.trim()) {
      setError('Task title is required');
      return;
    }

    setIsSubmitting(true);
    
    try {
      // Format the request data
      const requestData = {
        ...formData,
        dueDateTime: formData.dueDateTime ? new Date(formData.dueDateTime).toISOString() : null
      };
      
      // Submit form data
      const success = await onSubmit(requestData);
      
      if (success) {
        // Reset form if successful
        if (!isEditing) {
          setFormData({
            taskTitle: '',
            taskDescription: '',
            taskStatus: 'TODO',
            dueDateTime: ''
          });
        }
      }
    } catch (err) {
      setError('Failed to submit task. Please try again.');
      console.error('Form submission error:', err);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}
      
      <div>
        <label htmlFor="taskTitle" className="block text-sm font-medium text-gray-700 mb-1">
          Task Title*
        </label>
        <input
          type="text"
          id="taskTitle"
          name="taskTitle"
          value={formData.taskTitle}
          onChange={handleChange}
          className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          required
        />
      </div>
      
      <div>
        <label htmlFor="taskDescription" className="block text-sm font-medium text-gray-700 mb-1">
          Description
        </label>
        <textarea
          id="taskDescription"
          name="taskDescription"
          value={formData.taskDescription}
          onChange={handleChange}
          rows="3"
          className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>
      
      <div>
        <label htmlFor="taskStatus" className="block text-sm font-medium text-gray-700 mb-1">
          Status
        </label>
        <select
          id="taskStatus"
          name="taskStatus"
          value={formData.taskStatus}
          onChange={handleChange}
          className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="TODO">To Do</option>
          <option value="IN_PROGRESS">In Progress</option>
          <option value="COMPLETED">Completed</option>
        </select>
      </div>
      
      <div>
        <label htmlFor="dueDateTime" className="block text-sm font-medium text-gray-700 mb-1">
          Due Date & Time
        </label>
        <input
          type="datetime-local"
          id="dueDateTime"
          name="dueDateTime"
          value={formData.dueDateTime}
          onChange={handleChange}
          className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>
      
      <div className="flex justify-between pt-2">
        <button
          type="submit"
          disabled={isSubmitting}
          className={`px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 ${
            isSubmitting ? 'opacity-70 cursor-not-allowed' : ''
          }`}
        >
          {isSubmitting ? 'Saving...' : isEditing ? 'Update Task' : 'Add Task'}
        </button>
        
        {onCancel && (
          <button
            type="button"
            onClick={onCancel}
            className="px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500"
          >
            Cancel
          </button>
        )}
      </div>
    </form>
  );
};

export default TaskForm; 