-- ============================================================
-- TaskForge
-- V2 - Indexes and Search
--
-- Purpose:
--   Performance indexes and PostgreSQL trigram search indexes.
--
-- Design:
--   No tables or relationships are changed.
-- ============================================================


-- ============================================================
-- ORGANIZATION MEMBERS
-- ============================================================

CREATE INDEX idx_org_members_user
    ON organization_members(user_id);


-- ============================================================
-- ORGANIZATION INVITATIONS
-- ============================================================

CREATE INDEX idx_org_invitations_org_status
    ON organization_invitations(organization_id, status);


-- ============================================================
-- PROJECT ORGANIZATIONS
-- ============================================================

CREATE INDEX idx_project_orgs_organization
    ON project_organizations(organization_id);


-- ============================================================
-- PROJECT MEMBERS
-- ============================================================

CREATE INDEX idx_project_members_user
    ON project_members(user_id);


-- ============================================================
-- TASKS
-- ============================================================

CREATE INDEX idx_tasks_project
    ON tasks(project_id);

CREATE INDEX idx_tasks_project_status
    ON tasks(project_id, status);

CREATE INDEX idx_tasks_project_created
    ON tasks(project_id, created_at DESC);

CREATE INDEX idx_tasks_assigned_to
    ON tasks(assigned_to);

CREATE INDEX idx_tasks_due_date
    ON tasks(due_date);


-- ============================================================
-- TASK LABELS
-- ============================================================

CREATE INDEX idx_task_labels_label
    ON task_labels(label_id);


-- ============================================================
-- COMMENTS
-- ============================================================

CREATE INDEX idx_comments_task_created
    ON comments(task_id, created_at);


-- ============================================================
-- ATTACHMENTS
-- ============================================================

CREATE INDEX idx_attachments_task
    ON attachments(task_id);


-- ============================================================
-- NOTIFICATIONS
-- ============================================================

CREATE INDEX idx_notifications_user_read_created
    ON notifications(
                     user_id,
                     is_read,
                     created_at DESC
        );


-- ============================================================
-- AUDIT LOGS
-- ============================================================

CREATE INDEX idx_audit_logs_org_created
    ON audit_logs(
                  organization_id,
                  created_at DESC
        );

CREATE INDEX idx_audit_logs_entity
    ON audit_logs(
                  entity_type,
                  entity_id
        );


-- ============================================================
-- REFRESH TOKENS
-- ============================================================

CREATE INDEX idx_refresh_tokens_user
    ON refresh_tokens(user_id);


-- ============================================================
-- SEARCH INDEXES
-- PostgreSQL pg_trgm
-- ============================================================


-- ============================================================
-- USER FULL-NAME SEARCH
--
-- Example:
--   "jahanzaib wakeel"
--
-- Supports:
--   jahan
--   ahan
--   wakeel
--   jahanzaib
--   etc.
--
-- first_name and last_name are NOT NULL in V1,
-- so concatenation is safe.
--
-- lower(text) and text concatenation are IMMUTABLE,
-- allowing PostgreSQL to create the expression index.
-- ============================================================

CREATE INDEX idx_users_name_trgm
    ON users
    USING GIN (
    lower(first_name || ' ' || last_name) gin_trgm_ops
    );


-- ============================================================
-- PROJECT NAME SEARCH
-- ============================================================

CREATE INDEX idx_projects_name_trgm
    ON projects
    USING GIN (
    lower(name) gin_trgm_ops
    );


-- ============================================================
-- TASK TITLE SEARCH
-- ============================================================

CREATE INDEX idx_tasks_title_trgm
    ON tasks
    USING GIN (
    lower(title) gin_trgm_ops
    );


-- ============================================================
-- LABEL SEARCH
-- ============================================================

CREATE INDEX idx_labels_name_trgm
    ON labels
    USING GIN (
    lower(name) gin_trgm_ops
    );