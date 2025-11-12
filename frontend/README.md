# Frontend

## Environment Setup and Requirements
1. **Node.js** (version 18 or higher)
2. **npm** or **yarn** package manager
3. **VS Code** or any modern IDE
4. **Chrome Browser** (recommended for app usage and UI testing)

---

### Tech Stack & Frameworks
1. **Language:** TypeScript  
2. **Framework:** React (with Vite)  
3. **UI Library:** ShadCN UI + Tailwind CSS  
4. **Routing:** React Router DOM  
5. **Charts:** Recharts  
6. **Icons:** Lucide React  
7. **State Management:** React Hooks  
8. **Styling Utilities:** clsx, tailwind-merge  
9. **Build Tool:** Vite  
10. **Linting & Formatting:** ESLint + TypeScript  

---

### Folder Structure

```
frontend/
│
├── src/
│   ├── components/         # UI Components (Navbar, Dialogs, Cards, etc.)
│   ├── layouts/            # LandingLayout & MainLayout
│   ├── pages/              # Dashboard, Products, Sales, Reports, etc.
│   ├── routes/             # AppRoutes setup for navigation
│   ├── App.tsx             # Root component
│   ├── main.tsx            # Application entry point
│   └── index.css           # Tailwind base styles
│
├── public/                 # Static assets (images, icons, etc.)
├── package.json
└── tsconfig.json
```

---

### Running the Application

1. Open the **frontend** folder in VS Code or your preferred IDE  
2. Open the integrated terminal  
3. Install dependencies:
   ```bash
   npm install
   ```
4. Start the development server:
   ```bash
   npm run dev
   ```
5. Open the URL shown in the terminal (usually `http://localhost:5173/`)

   > The frontend will now run on port **5173** by default.

---


### Dependencies Overview

#### Core Dependencies
- **React 19** – Frontend library for UI rendering  
- **React Router DOM 7** – Routing and navigation  
- **Lucide React** – Icon library  
- **Recharts** – Data visualization for reports  
- **Tailwind CSS 4** – Utility-first styling  
- **UUID** – For generating unique identifiers  

#### Development Dependencies
- **Vite 7** – Lightning-fast dev server and build tool  
- **TypeScript 5.9** – Type-safe codebase  
- **ESLint 9** – Linting for code consistency  
- **tw-animate-css** – Animations for UI elements  
- **@vitejs/plugin-react** – Vite React plugin  

---

### Integration Notes

- The frontend connects to backend microservices via REST APIs.  
- Ensure all backend services (ports **8090–8095**) are running before using the frontend.  
- JWT tokens from the backend are stored in `localStorage` for authentication.

---


### Common Commands

| Command | Description |
|----------|-------------|
| `npm run dev` | Start development server |
| `npm run build` | Build production-ready app |
| `npm run preview` | Preview production build locally |
| `npm run lint` | Run ESLint checks |

---

### Troubleshooting

| Issue | Possible Fix |
|--------|----------------|
| `Port 5173 already in use` | Stop the existing Vite process or change the port in `vite.config.ts`. |
| `API requests failing` | Ensure backend microservices are running on ports 8090–8095  |

---

### Reports and Logs

Vite and React provide browser-based error overlays and console logs.  
For network requests, open **Chrome DevTools → Network tab** to inspect backend API calls.

---