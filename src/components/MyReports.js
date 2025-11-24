import React, { useState, useEffect } from 'react';
import { Container, Card, Row, Col, Badge, Spinner, Alert, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import apiClient from '../config/api';
import { useAuth } from '../context/AuthContext';

function MyReports() {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user, token } = useAuth();

  useEffect(() => {
    if (user) {
      fetchMyReports();
    }
  }, [user]);

  const fetchMyReports = async () => {
    try {
      setLoading(true);
      const response = await apiClient.get(`/reports/user/${user.userId}`);

      if (response.data.success) {
        setReports(response.data.data || []);
      } else {
        setError('Failed to load your reports');
      }
    } catch (err) {
      setError('Error loading reports: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status) => {
    const statusClass = `status-${status.toLowerCase()}`;
    return (
      <Badge className={`status-badge ${statusClass}`}>
        {status}
      </Badge>
    );
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  if (loading) {
    return (
      <Container className="text-center py-5">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
      </Container>
    );
  }

  return (
    <Container>
      <Row className="mb-4">
        <Col>
          <h2>My Reports</h2>
          <p className="text-muted">View and track your submitted scam reports</p>
        </Col>
        <Col md="auto">
          <Button as={Link} to="/submit-report" variant="primary">
            Submit New Report
          </Button>
        </Col>
      </Row>

      {error && <Alert variant="danger">{error}</Alert>}

      {reports.length === 0 ? (
        <Alert variant="info">
          You haven't submitted any reports yet.{' '}
          <Link to="/submit-report">Submit your first report</Link>
        </Alert>
      ) : (
        <Row>
          {reports.map((report) => (
            <Col md={6} lg={4} key={report.reportId} className="mb-4">
              <Card className="h-100">
                <Card.Header className="d-flex justify-content-between align-items-center">
                  <strong>{report.scamType}</strong>
                  {getStatusBadge(report.status)}
                </Card.Header>
                <Card.Body>
                  <Card.Title className="h6">{report.scamMethod}</Card.Title>
                  
                  {report.reportedName && (
                    <p className="mb-2">
                      <strong>Name:</strong> {report.reportedName}
                    </p>
                  )}
                  
                  {report.reportedPhone && (
                    <p className="mb-2">
                      <strong>Phone:</strong> {report.reportedPhone}
                    </p>
                  )}
                  
                  {report.reportedEmail && (
                    <p className="mb-2">
                      <strong>Email:</strong> {report.reportedEmail}
                    </p>
                  )}
                  
                  {report.reportedWebsite && (
                    <p className="mb-2">
                      <strong>Website:</strong> {report.reportedWebsite}
                    </p>
                  )}

                  <p className="mb-2 text-muted" style={{ fontSize: '0.9rem' }}>
                    {report.description.length > 100 
                      ? report.description.substring(0, 100) + '...' 
                      : report.description}
                  </p>

                  {report.amountLost && (
                    <p className="mb-2">
                      <strong>Amount Lost:</strong> ${parseFloat(report.amountLost).toFixed(2)}
                    </p>
                  )}

                  {report.location && (
                    <p className="mb-2">
                      <strong>Location:</strong> {report.location}
                    </p>
                  )}

                  <div className="mt-auto">
                    <small className="text-muted">
                      Submitted on: {formatDate(report.createdAt)}
                    </small>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </Container>
  );
}

export default MyReports;

