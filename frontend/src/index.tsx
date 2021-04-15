import { ReactElement, ReactNode, useEffect, lazy, Suspense } from 'react';
import ReactDOM from 'react-dom';
import 'assets/css/index.css';
import { StylesProvider } from '@material-ui/core';
import CssBaseline from '@material-ui/core/CssBaseline';
import { Navigate, BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import URLS from 'URLS';
import 'delayed-scroll-restoration-polyfill';

// Services
import { ThemeProvider } from 'hooks/ThemeContext';
import { SnackbarProvider } from 'hooks/Snackbar';
import { useUser } from 'hooks/User';

// Project components
import Navigation from 'components/navigation/Navigation';

// Project containers
import Landing from 'containers/Landing';
const Http404 = lazy(() => import('containers/Http404'));
const Auth = lazy(() => import('containers/Auth'));
const Activities = lazy(() => import('containers/Activities'));
const ActivityAdmin = lazy(() => import('containers/ActivityAdmin'));
const Profile = lazy(() => import('containers/Profile'));

type AuthRouteProps = {
  path: string;
  element?: ReactElement | null;
  children?: ReactNode;
};

const AuthRoute = ({ children, path, element }: AuthRouteProps) => {
  const { data, isLoading } = useUser();

  if (isLoading) {
    return <Navigation isLoading noFooter />;
  } else if (!data) {
    return <Navigate to={URLS.LOGIN} />;
  } else {
    return (
      <Route element={element} path={path}>
        {children}
      </Route>
    );
  }
};

type ProvidersProps = {
  children: ReactNode;
};

export const Providers = ({ children }: ProvidersProps) => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 1000 * 60 * 2, // Don't refetch data before 2 min has passed
        refetchOnWindowFocus: false,
      },
    },
  });
  return (
    <QueryClientProvider client={queryClient}>
      <StylesProvider injectFirst>
        <ThemeProvider>
          <CssBaseline />
          <SnackbarProvider>{children}</SnackbarProvider>
        </ThemeProvider>
      </StylesProvider>
      <ReactQueryDevtools />
    </QueryClientProvider>
  );
};

const AppRoutes = () => {
  const location = useLocation();
  useEffect(() => {
    window.gtag('event', 'page_view', {
      page_location: window.location.href,
      page_path: window.location.pathname,
    });
  }, [location]);
  return (
    <Routes>
      <Route element={<Landing />} path={URLS.LANDING} />
      <Route path={URLS.ACTIVITIES}>
        <Route element={<Activities />} path='' />
      </Route>
      <Route element={<Auth />} path={`${URLS.LOGIN}*`} />
      <AuthRoute element={<Profile />} path={`${URLS.PROFILE}*`} />
      <AuthRoute path={URLS.ADMIN_ACTIVITIES}>
        <Route element={<ActivityAdmin />} path=':activityId/' />
        <Route element={<ActivityAdmin />} path='' />
      </AuthRoute>

      <Route element={<Http404 />} path='*' />
    </Routes>
  );
};

export const Application = () => {
  return (
    <Providers>
      <BrowserRouter>
        <Suspense fallback={<Navigation isLoading />}>
          <AppRoutes />
        </Suspense>
      </BrowserRouter>
    </Providers>
  );
};

ReactDOM.render(<Application />, document.getElementById('root'));
