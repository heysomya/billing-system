# BillFast - Inventory Management System

## Overview
**BillFast** is a full-stack **Inventory Management System** designed to streamline retail operations by integrating product management, stock tracking, sales processing, and reporting into one platform.  
It supports multiple user roles with secure authentication and real-time data flow between frontend and backend services.

---

## Features

- **Product Catalog** – Add, edit, delete, and view products.  
- **Stock Management** – Track inventory levels and get low-stock alerts.  
- **Sales Recording** – Record each sale automatically and maintain transaction history.  
- **Billing System** – Generate purchase receipts automatically (PDF download supported).  
- **Reports** – View daily, weekly, and monthly sales summaries.  
- **Search & Filter** – Quickly find products by name, category, or SKU.  
- **User Roles** – Role-based access for Admins and Cashiers with authentication.  
- **Discount & Tax Management** – Apply discounts and taxes automatically to sales.  

---

## Tech Stack

| Layer | Technology |
|-------|-------------|
| Frontend | React + TypeScript + Vite + Tailwind CSS |
| Backend | Spring Boot + Java |
| Database | PostgreSQL (Supabase) |
| Communication | RESTful APIs |
| Authentication | JWT-based Security |

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/heysomya/billing-system.git
cd billing-system
```


### 2. Navigate to Project Parts

The repository contains three main folders — with the backend and frontend folders with its own README for setup and configuration details.
```
billfast/
│
├── automation/ # Automated testing
│
├── frontend/   # React + Vite web application
│   └── README.md
│
└── backend/    # Spring Boot microservices (user, product, sales, etc.)
    └── README.md
```

### 3. Follow Individual Setup Guides

Each part has a detailed README with its own installation steps:

Frontend:
```bash
cd frontend
```

Backend:
```bash
cd backend
```

### 4. Architecture
```
 ┌──────────────────────┐
 │      Frontend        │
 │  React + Tailwind UI │
 └──────────┬───────────┘
            │ REST API Calls
 ┌──────────▼────────────┐
 │       Backend         │
 │  Spring Boot Services │
 └──────────┬────────────┘
            │
 ┌──────────▼────────────┐
 │       Database        │
 │ PostgreSQL (Supabase) │
 └───────────────────────┘

```