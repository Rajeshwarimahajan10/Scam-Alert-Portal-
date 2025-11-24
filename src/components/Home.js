import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Home() {
  const { isAuthenticated } = useAuth();

  return (
    <Container>
      <Row className="mb-5">
        <Col>
          <div className="text-center py-5">
            <h1 className="display-4 mb-3">🚨 Scam Alert Portal</h1>
            <p className="lead">
              Report, track, and verify online and offline scam activities. 
              Stay informed and protect yourself from fraud.
            </p>
          </div>
        </Col>
      </Row>

      <Row className="mb-4">
        <Col md={4} className="mb-4">
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Title>
                <h3>🔍 Search Scams</h3>
              </Card.Title>
              <Card.Text>
                Check if a phone number, email, or website has been reported as a scam 
                before interacting with it.
              </Card.Text>
              <Button as={Link} to="/search" variant="primary">
                Search Now
              </Button>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4} className="mb-4">
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Title>
                <h3>📋 View Reports</h3>
              </Card.Title>
              <Card.Text>
                Browse all reported scam cases and stay aware of ongoing fraud activities 
                in your area.
              </Card.Text>
              <Button as={Link} to="/reports" variant="primary">
                View Reports
              </Button>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4} className="mb-4">
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Title>
                <h3>📝 Report Scam</h3>
              </Card.Title>
              <Card.Text>
                Help others by reporting scam incidents. Share details and help build 
                a safer community.
              </Card.Text>
              {isAuthenticated ? (
                <Button as={Link} to="/submit-report" variant="success">
                  Submit Report
                </Button>
              ) : (
                <Button as={Link} to="/login" variant="outline-success">
                  Login to Report
                </Button>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col>
          <Card className="bg-light">
            <Card.Body>
              <h4 className="mb-3">How It Works</h4>
              <Row>
                <Col md={3} className="mb-3">
                  <h5>1. Register</h5>
                  <p>Create an account to start reporting and tracking scams.</p>
                </Col>
                <Col md={3} className="mb-3">
                  <h5>2. Report</h5>
                  <p>Submit detailed information about scam incidents you've encountered.</p>
                </Col>
                <Col md={3} className="mb-3">
                  <h5>3. Verify</h5>
                  <p>Our team verifies reports to ensure accuracy and reliability.</p>
                </Col>
                <Col md={3} className="mb-3">
                  <h5>4. Stay Safe</h5>
                  <p>Search our database before engaging with unknown contacts or websites.</p>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default Home;

