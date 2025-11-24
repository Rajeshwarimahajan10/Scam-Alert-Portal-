import React, { useState } from 'react';
import { Container, Card, Form, Button, Row, Col, Badge, Alert, Spinner } from 'react-bootstrap';
import apiClient from '../config/api';

function Search() {
  const [searchType, setSearchType] = useState('phone');
  const [searchQuery, setSearchQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [searched, setSearched] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();
    
    if (!searchQuery.trim()) {
      setError('Please enter a search query');
      return;
    }

    setLoading(true);
    setError('');
    setSearched(true);

    try {
      let url = '';
      
      switch (searchType) {
        case 'phone':
          url = `/reports/search/phone?phone=${encodeURIComponent(searchQuery)}`;
          break;
        case 'email':
          url = `/reports/search/email?email=${encodeURIComponent(searchQuery)}`;
          break;
        case 'website':
          url = `/reports/search/website?website=${encodeURIComponent(searchQuery)}`;
          break;
        case 'keyword':
          url = `/reports/search/keyword?keyword=${encodeURIComponent(searchQuery)}`;
          break;
        default:
          url = `/reports/search/keyword?keyword=${encodeURIComponent(searchQuery)}`;
      }

      const response = await apiClient.get(url);
      
      if (response.data.success) {
        setResults(response.data.data || []);
        if (response.data.data.length === 0) {
          setError('No results found. This might be safe, but stay cautious!');
        }
      } else {
        setError('Search failed');
      }
    } catch (err) {
      setError('Error performing search: ' + (err.response?.data?.message || err.message));
      setResults([]);
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

  return (
    <Container>
      <Row className="mb-4">
        <Col>
          <h2>Search Scam Reports</h2>
          <p className="text-muted">
            Check if a phone number, email, website, or keyword has been reported as a scam
          </p>
        </Col>
      </Row>

      <Row className="justify-content-center mb-4">
        <Col md={8}>
          <Card>
            <Card.Body>
              <Form onSubmit={handleSearch}>
                <Form.Group className="mb-3">
                  <Form.Label>Search Type</Form.Label>
                  <Form.Select
                    value={searchType}
                    onChange={(e) => {
                      setSearchType(e.target.value);
                      setSearchQuery('');
                      setResults([]);
                      setSearched(false);
                    }}
                  >
                    <option value="phone">Phone Number</option>
                    <option value="email">Email Address</option>
                    <option value="website">Website URL</option>
                    <option value="keyword">Keyword Search</option>
                  </Form.Select>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    {searchType === 'phone' && 'Phone Number'}
                    {searchType === 'email' && 'Email Address'}
                    {searchType === 'website' && 'Website URL'}
                    {searchType === 'keyword' && 'Keyword'}
                  </Form.Label>
                  <Form.Control
                    type="text"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    placeholder={
                      searchType === 'phone' ? 'e.g., +1234567890' :
                      searchType === 'email' ? 'e.g., scam@example.com' :
                      searchType === 'website' ? 'e.g., https://example.com' :
                      'Enter keyword to search'
                    }
                    required
                  />
                </Form.Group>

                <div className="d-grid">
                  <Button variant="primary" type="submit" disabled={loading}>
                    {loading ? (
                      <>
                        <Spinner animation="border" size="sm" className="me-2" />
                        Searching...
                      </>
                    ) : (
                      'Search'
                    )}
                  </Button>
                </div>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {error && (
        <Alert variant={results.length === 0 && searched ? 'info' : 'danger'}>
          {error}
        </Alert>
      )}

      {results.length > 0 && (
        <Row>
          <Col>
            <h4 className="mb-3">
              Found {results.length} report{results.length !== 1 ? 's' : ''}
            </h4>
          </Col>
        </Row>
      )}

      {results.length > 0 && (
        <Row>
          {results.map((report) => (
            <Col md={6} lg={4} key={report.reportId} className="mb-4">
              <Card className="h-100 border-warning">
                <Card.Header className="d-flex justify-content-between align-items-center bg-warning bg-opacity-10">
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
                      Reported by: {report.username || 'Anonymous'} | {formatDate(report.createdAt)}
                    </small>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      )}

      {searched && results.length === 0 && !error && (
        <Alert variant="success">
          <strong>Good news!</strong> No reports found for this search. However, always stay cautious 
          and verify before sharing personal information or making payments.
        </Alert>
      )}
    </Container>
  );
}

export default Search;

