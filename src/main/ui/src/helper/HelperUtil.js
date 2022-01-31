export default function getFullUrl(uriPath) {
  let host = window.location.port === '3000' ? 'http://127.0.0.1:8080' : '';
  let finalUrl = host + uriPath
  console.debug('Final URL', finalUrl);
  return finalUrl;
}
