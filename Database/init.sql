

/* =========================================================
   ROLE TABLE
   Stores system roles such as admin, doctor, patient, etc.
   ========================================================= */
CREATE TABLE Rol(
  rol_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  -- Unique role name with restricted allowed values
  rol_name VARCHAR(50) UNIQUE NOT NULL
  CONSTRAINT valid_rol
    CHECK (rol_name IN ('admin', 'doctor', 'patient', 'receptionist' )),

  -- Optional description of the role
  rol_description TEXT,

  -- Logical status of the role
  rol_status BOOLEAN DEFAULT TRUE NOT NULL,

  -- Creation timestamp
  date_creation TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);
/* =========================================================
   INITIAL DATA SEEDING
   ========================================================= */


/* =========================================================
   USER TABLE
   Stores authentication and general user information
   ========================================================= */
CREATE TABLE user_tb(
  user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  -- Role assigned to the user
  rol_id INT NOT NULL,
  CONSTRAINT fk_user_rol
    FOREIGN KEY (rol_id)
    REFERENCES Rol(rol_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

  -- Unique username
  user_name VARCHAR(50) UNIQUE NOT NULL,

  -- Unique email with format validation
  user_email VARCHAR(100) UNIQUE NOT NULL,
  CONSTRAINT valid_email
    CHECK (user_email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),

  -- Encrypted password hash
  user_hash_password VARCHAR(255) NOT NULL,

  -- Logical status of the user
  user_status BOOLEAN DEFAULT TRUE NOT NULL,

  -- Account creation timestamp
  date_creation TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);


/* =========================================================
   PASSWORD RESET TOKENS
   Stores password recovery tokens for users
   ========================================================= */
CREATE TABLE password_reset_tokens (
  token_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  -- Associated user
  user_id INT NOT NULL,

  -- Hashed reset token
  token_hash VARCHAR(255) NOT NULL,

  -- Token expiration date
  expiry TIMESTAMPTZ NOT NULL,

  -- Indicates whether the token has been used
  is_used BOOLEAN DEFAULT FALSE NOT NULL,

  -- Token creation timestamp
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_token_user
    FOREIGN KEY (user_id)
    REFERENCES user_tb(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


/* =========================================================
   PATIENT TABLE
   Stores personal and contact information for patients
   ========================================================= */
CREATE TABLE patient_tb(
  patient_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  -- Linked user account (one-to-one)
  user_id INT UNIQUE NOT NULL,

  first_name VARCHAR(50) NOT NULL,
  second_name VARCHAR(50),
  middle_name VARCHAR(50),
  last_name VARCHAR(50) NOT NULL,

  date_of_birth DATE NOT NULL,

  -- Sex: Female, Male, Other
  sex CHAR(1) NOT NULL,

  -- Unique phone number with format validation
  phone_number VARCHAR(15) UNIQUE NOT NULL,

  CONSTRAINT fk_patient_user
    FOREIGN KEY (user_id)
    REFERENCES user_tb(user_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

  CONSTRAINT chk_sex
    CHECK (sex IN ('F', 'M', 'O')),

  CONSTRAINT chk_phone_number
    CHECK (phone_number ~ '^[0-9\+][0-9\s\-\(\)]{7,}$')
);


/* =========================================================
   SPECIALIZATION TABLE
   Stores medical specialties
   ========================================================= */
CREATE TABLE specialization_tb(
  specialization_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  specialization_name VARCHAR(100) UNIQUE NOT NULL,
  specialization_description TEXT,

  date_creation TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  -- Logical status of the specialization
  status BOOLEAN DEFAULT TRUE NOT NULL
);


/* =========================================================
   DOCTOR TABLE
   Stores doctor personal information
   ========================================================= */
CREATE TABLE doctor_tb(
  doctor_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  -- Linked user account (one-to-one)
  user_id INT UNIQUE NOT NULL,

  first_name VARCHAR(50) NOT NULL,
  second_name VARCHAR(50),
  middle_name VARCHAR(50),
  last_name VARCHAR(50) NOT NULL,

  -- Doctor availability status
  status BOOLEAN DEFAULT TRUE NOT NULL,

  CONSTRAINT fk_doctor_user
    FOREIGN KEY (user_id)
    REFERENCES user_tb(user_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);


/* =========================================================
   DOCTOR - SPECIALIZATION (MANY TO MANY)
   ========================================================= */
CREATE TABLE doctor_specialization_tb (
  doctor_id INT NOT NULL,
  specialization_id INT NOT NULL,

  -- Date when the specialization was assigned
  date_assigned TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  PRIMARY KEY (doctor_id, specialization_id),

  CONSTRAINT fk_doctor
    FOREIGN KEY (doctor_id)
    REFERENCES doctor_tb(doctor_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT fk_specialization
    FOREIGN KEY (specialization_id)
    REFERENCES specialization_tb(specialization_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);


/* =========================================================
   DOCTOR AVAILABILITY
   Defines specific availability slots for doctors
   ========================================================= */
CREATE TABLE doctor_availability(
  availability_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  doctor_id INT NOT NULL,

  available_date DATE NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,

  is_available BOOLEAN DEFAULT TRUE NOT NULL,

  date_creation TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT chk_availability_time
    CHECK (start_time < end_time),

  CONSTRAINT fk_availability_doctor
    FOREIGN KEY (doctor_id)
    REFERENCES doctor_tb(doctor_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);


/* =========================================================
   SCHEDULE TEMPLATES
   Defines recurring weekly schedules for doctors
   ========================================================= */
CREATE TABLE schedule_templates(
  template_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  doctor_id INT NOT NULL,

  day_of_week VARCHAR(10) NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,

  is_active BOOLEAN DEFAULT TRUE NOT NULL,

  date_creation TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT chk_template_time
    CHECK (start_time < end_time),

  CONSTRAINT chk_day_of_week
    CHECK (day_of_week IN (
      'Monday', 'Tuesday', 'Wednesday',
      'Thursday', 'Friday', 'Saturday', 'Sunday'
    )),

  CONSTRAINT fk_template_doctor
    FOREIGN KEY (doctor_id)
    REFERENCES doctor_tb(doctor_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);


/* =========================================================
   APPOINTMENTS
   Manages patient appointments with doctors
   ========================================================= */
CREATE TABLE appointments(
  appointment_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

  patient_id INT NOT NULL,
  availability_id INT NOT NULL,

  appointment_start_time TIMESTAMPTZ NOT NULL,
  appointment_end_time TIMESTAMPTZ NOT NULL,

  reason TEXT,

  -- Appointment lifecycle status
  appointment_status VARCHAR(20) DEFAULT 'scheduled' NOT NULL,

  notes TEXT,

  date_creation TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  last_updated TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_appointment_patient
    FOREIGN KEY (patient_id)
    REFERENCES patient_tb(patient_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

  CONSTRAINT fk_appointment_availability
    FOREIGN KEY (availability_id)
    REFERENCES doctor_availability(availability_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

  CONSTRAINT chk_appointment_status
    CHECK (appointment_status IN (
      'scheduled', 'confirmed', 'in_progress',
      'completed', 'cancelled', 'no_show'
    )),

  CONSTRAINT chk_appointment_date
    CHECK (appointment_start_time < appointment_end_time)
);



-- Insertar Roles obligatorios
INSERT INTO Rol (rol_name, rol_description) VALUES
('admin', 'Administrator with full access'),
('doctor', 'Medical staff'),
('patient', 'Default user role for patients'),
('receptionist', 'Administrative staff');
