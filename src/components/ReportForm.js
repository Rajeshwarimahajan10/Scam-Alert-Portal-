import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Card, Form, Button, Alert, Row, Col } from 'react-bootstrap';
import apiClient from '../config/api';
import { useAuth } from '../context/AuthContext';

function ReportForm() {
  const [formData, setFormData] = useState({
    scamType: '',
    scamMethod: '',
    reportedPhone: '',
    reportedEmail: '',
    reportedWebsite: '',
    reportedName: '',
    description: '',
    amountLost: '',
    incidentDate: '',
    location: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  
  const { user, token } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    if (!formData.scamType || !formData.scamMethod || !formData.description) {
      setError('Please fill in all required fields');
      setLoading(false);
      return;
    }

    if (!formData.reportedPhone && !formData.reportedEmail && !formData.reportedWebsite) {
      setError('Please provide at least one contact detail (Phone, Email, or Website)');
      setLoading(false);
      return;
    }

    const reportData = {
      ...formData,
      userId: user.userId,
      amountLost: formData.amountLost || null
    };

    try {
      const response = await apiClient.post('/reports', reportData);

      if (response.data.success) {
        setSuccess('Report submitted successfully! Redirecting...');
        setTimeout(() => {
          navigate('/my-reports');
        }, 2000);
      } else {
        setError(response.data.message || 'Failed to submit report');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Error submitting report');
    }
    
    setLoading(false);
  };

  return (
    <Container>
      <Row className="justify-content-center">
        <Col md={10} lg={8}>
          <Card>
            <Card.Header>
              <h3 className="mb-0">Submit Scam Report</h3>
            </Card.Header>
            <Card.Body>
              {error && <Alert variant="danger">{error}</Alert>}
              {success && <Alert variant="success">{success}</Alert>}

              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Scam Type *</Form.Label>
                      <Form.Select
                        name="scamType"
                        value={formData.scamType}
                        onChange={handleChange}
                        required
                      >
                        <option value="">Select scam type</option>
                        <option value="Phone Call">Phone Call</option>
                        <option value="Email">Email</option>
                        <option value="SMS">SMS</option>
                        <option value="Website">Website</option>
                        <option value="Social Media">Social Media</option>
                        <option value="Investment">Investment</option>
                        <option value="Job Offer">Job Offer</option>
                        <option value="Lottery">Lottery</option>
                        <option value="Romance">Romance</option>
                        <option value="Other">Other</option>
                      </Form.Select>
                    </Form.Group>
                  </Col>

                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Scam Method *</Form.Label>
                      <Form.Select
                        name="scamMethod"
                        value={formData.scamMethod}
                        onChange={handleChange}
                        required
                      >
                        <option value="">Select method</option>
                        <option value="Phishing">Phishing</option>
                        <option value="Identity Theft">Identity Theft</option>
                        <option value="Fake Website">Fake Website</option>
                        <option value="Fake Call">Fake Call</option>
                        <option value="Money Request">Money Request</option>
                        <option value="Fake Product">Fake Product</option>
                        <option value="Other">Other</option>
                      </Form.Select>
                    </Form.Group>
                  </Col>
                </Row>

                <Row>
                  <Col md={4}>
                    <Form.Group className="mb-3">
                      <Form.Label>Reported Phone Number</Form.Label>
                      <Form.Control
                        type="tel"
                        name="reportedPhone"
                        value={formData.reportedPhone}
                        onChange={handleChange}
                        placeholder="e.g., +1234567890"
                      />
                    </Form.Group>
                  </Col>

                  <Col md={4}>
                    <Form.Group className="mb-3">
                      <Form.Label>Reported Email</Form.Label>
                      <Form.Control
                        type="email"
                        name="reportedEmail"
                        value={formData.reportedEmail}
                        onChange={handleChange}
                        placeholder="e.g., scam@example.com"
                      />
                    </Form.Group>
                  </Col>

                  <Col md={4}>
                    <Form.Group className="mb-3">
                      <Form.Label>Reported Website</Form.Label>
                      <Form.Control
                        type="url"
                        name="reportedWebsite"
                        value={formData.reportedWebsite}
                        onChange={handleChange}
                        placeholder="e.g., https://scam-site.com"
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label>Reported Name/Organization</Form.Label>
                  <Form.Control
                    type="text"
                    name="reportedName"
                    value={formData.reportedName}
                    onChange={handleChange}
                    placeholder="Name of the person or organization"
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Description *</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={5}
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    placeholder="Provide detailed description of the scam incident..."
                    required
                  />
                </Form.Group>

                <Row>
                  <Col md={4}>
                    <Form.Group className="mb-3">
                      <Form.Label>Amount Lost (if any)</Form.Label>
                      <Form.Control
                        type="number"
                        step="0.01"
                        name="amountLost"
                        value={formData.amountLost}
                        onChange={handleChange}
                        placeholder="0.00"
                        min="0"
                      />
                    </Form.Group>
                  </Col>

                  <Col md={4}>
                    <Form.Group className="mb-3">
                      <Form.Label>Incident Date</Form.Label>
                      <Form.Control
                        type="date"
                        name="incidentDate"
                        value={formData.incidentDate}
                        onChange={handleChange}
                      />
                    </Form.Group>
                  </Col>

                  <Col md={4}>
                    <Form.Group className="mb-3">
                      <Form.Label>Location</Form.Label>
                      <Form.Control
                        type="text"
                        name="location"
                        value={formData.location}
                        onChange={handleChange}
                        placeholder="City, State/Country"
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <div className="d-grid gap-2">
                  <Button 
                    variant="primary" 
                    type="submit" 
                    size="lg"
                    disabled={loading}
                  >
                    {loading ? 'Submitting...' : 'Submit Report'}
                  </Button>
                </div>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default ReportForm;

