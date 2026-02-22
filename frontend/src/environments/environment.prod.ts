export const environment = {
    production: true,
    apiUrl: 'https://api.smartlogi.com/api',
    oAuth2: {
        googleClientId: 'YOUR_GOOGLE_CLIENT_ID_PROD',
        githubClientId: 'YOUR_GITHUB_CLIENT_ID_PROD',
        redirectUri: 'https://app.smartlogi.com/oauth/callback'
    },
    tokenKey: 'smartlogi_token',
    refreshTokenKey: 'smartlogi_refresh_token'
};
