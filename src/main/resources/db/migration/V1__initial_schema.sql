-- ============================================================
-- TaskForge
-- V1 - Initial Database Schema
--
-- Purpose:
--   Core schema for:
--   - Users
--   - Organizations
--   - Organization membership
--   - Organization invitations
--   - Projects
--   - Cross-organization collaboration
--   - Project membership
--   - Tasks
--   - Labels
--   - Comments
--   - Attachments
--   - Notifications
--   - Email deliveries
--   - Refresh tokens
--   - Audit logs
-- ============================================================


-- ============================================================
-- PostgreSQL Extensions
-- ============================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE EXTENSION IF NOT EXISTS citext;

CREATE EXTENSION IF NOT EXISTS pg_trgm;


-- ============================================================
-- 1. USERS
-- ============================================================

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       email CITEXT NOT NULL,

                       password_hash VARCHAR(255) NOT NULL,

                       first_name VARCHAR(100) NOT NULL,

                       last_name VARCHAR(100) NOT NULL,

                       status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT uq_users_email
                           UNIQUE (email),

                       CONSTRAINT chk_users_status
                           CHECK (
                               status IN (
                                          'ACTIVE',
                                          'INACTIVE',
                                          'SUSPENDED'
                                   )
                               )
);


-- ============================================================
-- 2. ORGANIZATIONS
-- ============================================================

CREATE TABLE organizations (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                               name VARCHAR(255) NOT NULL,

                               slug VARCHAR(100) NOT NULL,

                               created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT uq_organizations_slug
                                   UNIQUE (slug)
);


-- ============================================================
-- 3. ORGANIZATION MEMBERS
--
-- Answers:
-- "Who belongs to this organization?"
--
-- platform_role = TaskForge permissions
-- job_title     = actual company position
-- department    = company department
-- ============================================================

CREATE TABLE organization_members (
                                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                      organization_id UUID NOT NULL,

                                      user_id UUID NOT NULL,

                                      platform_role VARCHAR(20) NOT NULL DEFAULT 'MEMBER',

                                      job_title VARCHAR(100),

                                      department VARCHAR(100),

                                      joined_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                      CONSTRAINT fk_org_members_organization
                                          FOREIGN KEY (organization_id)
                                              REFERENCES organizations(id)
                                              ON DELETE CASCADE,

                                      CONSTRAINT fk_org_members_user
                                          FOREIGN KEY (user_id)
                                              REFERENCES users(id)
                                              ON DELETE RESTRICT,

                                      CONSTRAINT uq_org_members_user
                                          UNIQUE (organization_id, user_id),

                                      CONSTRAINT chk_org_members_role
                                          CHECK (
                                              platform_role IN (
                                                                'OWNER',
                                                                'ADMIN',
                                                                'MANAGER',
                                                                'MEMBER'
                                                  )
                                              )
);


-- ============================================================
-- 4. ORGANIZATION INVITATIONS
--
-- Used when an organization wants to add a person.
--
-- Example:
--
-- ABC Admin
--     |
--     | Invite Ali
--     v
-- Ali receives email
--     |
--     +--> ACCEPT
--     |
--     +--> REJECT
-- ============================================================

CREATE TABLE organization_invitations (
                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                          organization_id UUID NOT NULL,

                                          email CITEXT NOT NULL,

                                          invited_by UUID NOT NULL,

                                          platform_role VARCHAR(20) NOT NULL DEFAULT 'MEMBER',

                                          job_title VARCHAR(100),

                                          department VARCHAR(100),

                                          token_hash VARCHAR(255) NOT NULL,

                                          status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

                                          expires_at TIMESTAMPTZ NOT NULL,

                                          accepted_by UUID,

                                          accepted_at TIMESTAMPTZ,

                                          created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                          CONSTRAINT fk_org_invites_organization
                                              FOREIGN KEY (organization_id)
                                                  REFERENCES organizations(id)
                                                  ON DELETE CASCADE,

                                          CONSTRAINT fk_org_invites_invited_by
                                              FOREIGN KEY (invited_by)
                                                  REFERENCES users(id)
                                                  ON DELETE RESTRICT,

                                          CONSTRAINT fk_org_invites_accepted_by
                                              FOREIGN KEY (accepted_by)
                                                  REFERENCES users(id)
                                                  ON DELETE SET NULL,

                                          CONSTRAINT uq_org_invites_token
                                              UNIQUE (token_hash),

                                          CONSTRAINT chk_org_invites_role
                                              CHECK (
                                                  platform_role IN (
                                                                    'ADMIN',
                                                                    'MANAGER',
                                                                    'MEMBER'
                                                      )
                                                  ),

                                          CONSTRAINT chk_org_invites_status
                                              CHECK (
                                                  status IN (
                                                             'PENDING',
                                                             'ACCEPTED',
                                                             'REJECTED',
                                                             'EXPIRED',
                                                             'REVOKED'
                                                      )
                                                  )
);


-- ============================================================
-- 5. PROJECTS
--
-- A project is the main unit of work.
--
-- Projects do NOT directly contain organization_id.
-- Organization relationships are represented by
-- project_organizations.
-- ============================================================

CREATE TABLE projects (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                          name VARCHAR(255) NOT NULL,

                          slug VARCHAR(100) NOT NULL,

                          description TEXT,

                          status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

                          created_by UUID NOT NULL,

                          created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT uq_projects_slug
                              UNIQUE (slug),

                          CONSTRAINT fk_projects_created_by
                              FOREIGN KEY (created_by)
                                  REFERENCES users(id)
                                  ON DELETE RESTRICT,

                          CONSTRAINT chk_projects_status
                              CHECK (
                                  status IN (
                                             'ACTIVE',
                                             'ARCHIVED'
                                      )
                                  )
);


-- ============================================================
-- 6. PROJECT COLLABORATION REQUESTS
--
-- Used when one organization wants another organization
-- to participate in a project.
--
-- Example:
--
-- ABC Technologies
--       |
--       | Collaboration Request
--       v
-- XYZ Bank
--       |
--       +--> ACCEPT
--       |
--       +--> REJECT
--
-- Only after ACCEPTED do we establish the actual
-- project_organizations relationship.
-- ============================================================

CREATE TABLE project_collaboration_requests (
                                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                                project_id UUID NOT NULL,

                                                requesting_organization_id UUID NOT NULL,

                                                target_organization_id UUID NOT NULL,

                                                requested_by UUID NOT NULL,

                                                status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

                                                responded_by UUID,

                                                responded_at TIMESTAMPTZ,

                                                message TEXT,

                                                created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                                CONSTRAINT fk_collab_requests_project
                                                    FOREIGN KEY (project_id)
                                                        REFERENCES projects(id)
                                                        ON DELETE CASCADE,

                                                CONSTRAINT fk_collab_requests_requesting_org
                                                    FOREIGN KEY (requesting_organization_id)
                                                        REFERENCES organizations(id)
                                                        ON DELETE CASCADE,

                                                CONSTRAINT fk_collab_requests_target_org
                                                    FOREIGN KEY (target_organization_id)
                                                        REFERENCES organizations(id)
                                                        ON DELETE CASCADE,

                                                CONSTRAINT fk_collab_requests_requested_by
                                                    FOREIGN KEY (requested_by)
                                                        REFERENCES users(id)
                                                        ON DELETE RESTRICT,

                                                CONSTRAINT fk_collab_requests_responded_by
                                                    FOREIGN KEY (responded_by)
                                                        REFERENCES users(id)
                                                        ON DELETE SET NULL,

                                                CONSTRAINT chk_collab_requests_different_orgs
                                                    CHECK (
                                                        requesting_organization_id <> target_organization_id
                                                        ),

                                                CONSTRAINT chk_collab_requests_status
                                                    CHECK (
                                                        status IN (
                                                                   'PENDING',
                                                                   'ACCEPTED',
                                                                   'REJECTED',
                                                                   'CANCELLED'
                                                            )
                                                        )
);


-- ============================================================
-- 7. PROJECT ORGANIZATIONS
--
-- Represents an ACTIVE collaboration.
--
-- Example:
--
-- Banking Application
--     |
--     +--> ABC Technologies = OWNER
--     |
--     +--> XYZ Bank         = PARTNER
-- ============================================================

CREATE TABLE project_organizations (
                                       project_id UUID NOT NULL,

                                       organization_id UUID NOT NULL,

                                       role VARCHAR(20) NOT NULL,

                                       joined_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                       PRIMARY KEY (project_id, organization_id),

                                       CONSTRAINT fk_project_orgs_project
                                           FOREIGN KEY (project_id)
                                               REFERENCES projects(id)
                                               ON DELETE CASCADE,

                                       CONSTRAINT fk_project_orgs_organization
                                           FOREIGN KEY (organization_id)
                                               REFERENCES organizations(id)
                                               ON DELETE CASCADE,

                                       CONSTRAINT chk_project_orgs_role
                                           CHECK (
                                               role IN (
                                                        'OWNER',
                                                        'PARTNER'
                                                   )
                                               )
);


-- ============================================================
-- 8. PROJECT MEMBERS
--
-- Represents people assigned to participate in a project.
--
-- The person must:
--
-- 1. Belong to the organization
-- 2. The organization must participate in the project
-- ============================================================

CREATE TABLE project_members (
                                 project_id UUID NOT NULL,

                                 user_id UUID NOT NULL,

                                 organization_id UUID NOT NULL,

                                 role VARCHAR(30) NOT NULL DEFAULT 'CONTRIBUTOR',

                                 joined_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                 PRIMARY KEY (project_id, user_id),

                                 CONSTRAINT fk_project_members_project_org
                                     FOREIGN KEY (project_id, organization_id)
                                         REFERENCES project_organizations(project_id, organization_id)
                                         ON DELETE CASCADE,

                                 CONSTRAINT fk_project_members_org_user
                                     FOREIGN KEY (organization_id, user_id)
                                         REFERENCES organization_members(organization_id, user_id)
                                         ON DELETE RESTRICT,

                                 CONSTRAINT chk_project_members_role
                                     CHECK (
                                         role IN (
                                                  'PROJECT_ADMIN',
                                                  'PROJECT_MANAGER',
                                                  'LEAD',
                                                  'CONTRIBUTOR',
                                                  'VIEWER'
                                             )
                                         )
);


-- ============================================================
-- 9. TASKS
--
-- Actual work assigned to individual people.
-- ============================================================

CREATE TABLE tasks (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       project_id UUID NOT NULL,

                       title VARCHAR(255) NOT NULL,

                       description TEXT,

                       status VARCHAR(20) NOT NULL DEFAULT 'TODO',

                       priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',

                       created_by UUID NOT NULL,

                       assigned_to UUID,

                       due_date TIMESTAMPTZ,

                       version INTEGER NOT NULL DEFAULT 0,

                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_tasks_project
                           FOREIGN KEY (project_id)
                               REFERENCES projects(id)
                               ON DELETE RESTRICT,

                       CONSTRAINT fk_tasks_created_by
                           FOREIGN KEY (created_by)
                               REFERENCES users(id)
                               ON DELETE RESTRICT,

                       CONSTRAINT fk_tasks_assigned_to
                           FOREIGN KEY (assigned_to)
                               REFERENCES users(id)
                               ON DELETE SET NULL,

                       CONSTRAINT chk_tasks_status
                           CHECK (
                               status IN (
                                          'TODO',
                                          'IN_PROGRESS',
                                          'IN_REVIEW',
                                          'DONE',
                                          'CANCELLED'
                                   )
                               ),

                       CONSTRAINT chk_tasks_priority
                           CHECK (
                               priority IN (
                                            'LOW',
                                            'MEDIUM',
                                            'HIGH',
                                            'URGENT'
                                   )
                               ),

                       CONSTRAINT chk_tasks_version
                           CHECK (
                               version >= 0
                               )
);


-- ============================================================
-- 10. LABELS
-- ============================================================

CREATE TABLE labels (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                        organization_id UUID NOT NULL,

                        name VARCHAR(100) NOT NULL,

                        color VARCHAR(20),

                        created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_labels_organization
                            FOREIGN KEY (organization_id)
                                REFERENCES organizations(id)
                                ON DELETE CASCADE,

                        CONSTRAINT uq_labels_org_name
                            UNIQUE (organization_id, name)
);


-- ============================================================
-- 11. TASK LABELS
--
-- Many-to-many:
--
-- Task <----> Label
-- ============================================================

CREATE TABLE task_labels (
                             task_id UUID NOT NULL,

                             label_id UUID NOT NULL,

                             PRIMARY KEY (task_id, label_id),

                             CONSTRAINT fk_task_labels_task
                                 FOREIGN KEY (task_id)
                                     REFERENCES tasks(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_task_labels_label
                                 FOREIGN KEY (label_id)
                                     REFERENCES labels(id)
                                     ON DELETE CASCADE
);


-- ============================================================
-- 12. COMMENTS
-- ============================================================

CREATE TABLE comments (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                          task_id UUID NOT NULL,

                          user_id UUID NOT NULL,

                          content TEXT NOT NULL,

                          created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_comments_task
                              FOREIGN KEY (task_id)
                                  REFERENCES tasks(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_comments_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
                                  ON DELETE RESTRICT
);


-- ============================================================
-- 13. ATTACHMENTS
--
-- Database stores metadata.
-- Actual files will eventually live in object storage.
-- ============================================================

CREATE TABLE attachments (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                             task_id UUID NOT NULL,

                             uploaded_by UUID NOT NULL,

                             file_name VARCHAR(255) NOT NULL,

                             object_key VARCHAR(500) NOT NULL,

                             content_type VARCHAR(100) NOT NULL,

                             file_size BIGINT NOT NULL,

                             created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_attachments_task
                                 FOREIGN KEY (task_id)
                                     REFERENCES tasks(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_attachments_uploaded_by
                                 FOREIGN KEY (uploaded_by)
                                     REFERENCES users(id)
                                     ON DELETE RESTRICT,

                             CONSTRAINT chk_attachments_file_size
                                 CHECK (
                                     file_size >= 0
                                     )
);


-- ============================================================
-- 14. NOTIFICATIONS
--
-- In-app notifications.
-- ============================================================

CREATE TABLE notifications (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                               user_id UUID NOT NULL,

                               type VARCHAR(50) NOT NULL,

                               title VARCHAR(255) NOT NULL,

                               message TEXT NOT NULL,

                               entity_type VARCHAR(50),

                               entity_id UUID,

                               is_read BOOLEAN NOT NULL DEFAULT FALSE,

                               created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               read_at TIMESTAMPTZ,

                               CONSTRAINT fk_notifications_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE
);


-- ============================================================
-- 15. EMAIL DELIVERIES
--
-- Tracks emails generated by TaskForge.
--
-- Example:
--
-- Task assigned
--      |
--      +--> In-app notification
--      |
--      +--> Email delivery
-- ============================================================

CREATE TABLE email_deliveries (
                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                  user_id UUID,

                                  notification_id UUID,

                                  recipient_email CITEXT NOT NULL,

                                  email_type VARCHAR(50) NOT NULL,

                                  subject VARCHAR(255) NOT NULL,

                                  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

                                  attempts INTEGER NOT NULL DEFAULT 0,

                                  sent_at TIMESTAMPTZ,

                                  last_error TEXT,

                                  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_email_deliveries_user
                                      FOREIGN KEY (user_id)
                                          REFERENCES users(id)
                                          ON DELETE SET NULL,

                                  CONSTRAINT fk_email_deliveries_notification
                                      FOREIGN KEY (notification_id)
                                          REFERENCES notifications(id)
                                          ON DELETE SET NULL,

                                  CONSTRAINT chk_email_delivery_status
                                      CHECK (
                                          status IN (
                                                     'PENDING',
                                                     'SENDING',
                                                     'SENT',
                                                     'FAILED'
                                              )
                                          ),

                                  CONSTRAINT chk_email_delivery_attempts
                                      CHECK (
                                          attempts >= 0
                                          )
);


-- ============================================================
-- 16. REFRESH TOKENS
-- ============================================================

CREATE TABLE refresh_tokens (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                user_id UUID NOT NULL,

                                token_hash VARCHAR(255) NOT NULL,

                                expires_at TIMESTAMPTZ NOT NULL,

                                revoked_at TIMESTAMPTZ,

                                created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_refresh_tokens_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES users(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT uq_refresh_tokens_hash
                                    UNIQUE (token_hash)
);


-- ============================================================
-- 17. AUDIT LOGS
--
-- Records important security/business actions.
-- ============================================================

CREATE TABLE audit_logs (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                            organization_id UUID,

                            user_id UUID,

                            action VARCHAR(100) NOT NULL,

                            entity_type VARCHAR(50) NOT NULL,

                            entity_id UUID NOT NULL,

                            old_values JSONB,

                            new_values JSONB,

                            ip_address INET,

                            user_agent TEXT,

                            created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_audit_logs_organization
                                FOREIGN KEY (organization_id)
                                    REFERENCES organizations(id)
                                    ON DELETE SET NULL,

                            CONSTRAINT fk_audit_logs_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE SET NULL
);


-- ============================================================
-- PROJECT OWNER CONSTRAINT
--
-- Every project can have only one OWNER organization.
-- ============================================================

CREATE UNIQUE INDEX uq_project_owner
    ON project_organizations(project_id)
    WHERE role = 'OWNER';