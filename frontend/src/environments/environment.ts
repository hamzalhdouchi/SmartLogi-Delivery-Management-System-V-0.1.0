export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  oAuth2: {
    googleClientId: 'YOUR_GOOGLE_CLIENT_ID',
    githubClientId: 'YOUR_GITHUB_CLIENT_ID',
    redirectUri: 'http://localhost:4200/oauth/callback'
  },
  tokenKey: 'smartlogi_token',
  refreshTokenKey: 'smartlogi_refresh_token'
};
